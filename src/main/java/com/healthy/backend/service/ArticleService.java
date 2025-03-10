package com.healthy.backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.healthy.backend.dto.article.ArticleRequest;
import com.healthy.backend.dto.article.ArticleResponse;
import com.healthy.backend.entity.Article;
import com.healthy.backend.entity.Tags;
import com.healthy.backend.entity.Users;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.ArticleMapper;
import com.healthy.backend.repository.ArticleRepository;
import com.healthy.backend.repository.TagsRepository;
import com.healthy.backend.repository.UserRepository;
import com.healthy.backend.security.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final GeneralService __;
    private final ArticleMapper articleMapper;
    private final TokenService tokenService;
    private final TagsRepository tagsRepository;

    public void createArticle(HttpServletRequest requestUser  ,ArticleRequest request) {
        Role role = tokenService.retrieveUser(requestUser).getRole();
        switch (role) {
            case MANAGER:
            case PSYCHOLOGIST:
                

                Article article = Article.builder()
                .articleID(__.generateArticleID())
                .title(request.getTitle())
                .content(request.getContent())
                .author(tokenService.retrieveUser(requestUser))
                .articleTag(setArticleTag(request))
                .likes(0)
                .build();
                articleRepository.save(article);
            return;           
        default:
            throw new RuntimeException("You don't have permission to access this resource.");
        }
    }

    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAll()
                .stream().map(articleMapper::mapToResponse)
                .toList();
    }

    public ArticleResponse getArticleById(String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        return articleMapper.mapToResponse(article);
    }

    public void updateArticle(String articleId, HttpServletRequest requestUser, ArticleRequest request) {
        Role role = tokenService.retrieveUser(requestUser).getRole();
        
        switch (role) {
            case MANAGER:
            case PSYCHOLOGIST:

         Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());  
        
        article.setArticleTag(setArticleTag(request));

        articleRepository.save(article);
             
        default:
            throw new RuntimeException("You don't have permission to access this resource.");
        }
        
    }

    public HashSet<Tags> setArticleTag(ArticleRequest request) {
    HashSet<Tags> articleTags = new HashSet<>();

    if (request.getTags() != null) {
        for (String tagName : request.getTags()) {
            Tags tag = tagsRepository.findByTagName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag not found: " + tagName));
            articleTags.add(tag);
        }
    }
    return articleTags;
    }   


    public void deleteArticle(HttpServletRequest requestUser, String articleId) {
        Role role = tokenService.retrieveUser(requestUser).getRole();
        
        switch (role) {
            case MANAGER:

            if (!articleRepository.existsById(articleId)) {
                throw new RuntimeException("Article not found");
            }
            articleRepository.deleteById(articleId);
             
        default:
            throw new RuntimeException("You don't have permission to access this resource.");
        }        
        
        
        
    }

    public List<ArticleResponse> getArticlesByTag(String tagName) {
        List<Tags> tags = tagsRepository.findKeyWordInTag(tagName);

        Set<Article> setArticle = tags.stream()
            .flatMap(art -> art.getArticles().stream()) 
            .collect(Collectors.toSet());

        List<ArticleResponse> articleResponses = setArticle.stream()
            .map(article -> {
                return articleMapper.mapToResponse(article);
            })   
            .collect(Collectors.toList()); 

        return articleResponses;    
    }
    

    public List<ArticleResponse> getArticlesByAuthor(String authorId) {
        return articleRepository.findArticleByAuthor(authorId)
                .stream().map(articleMapper::mapToResponse)
                .toList();
    }

    public List<ArticleResponse> searchArticles(String keyword) {
        return articleRepository.searchByKeyWordInTitle(keyword)
                .stream().map(articleMapper::mapToResponse)
                .toList();
    }

    public void likeArticle(String id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        article.setLikes(article.getLikes() + 1);
        articleRepository.save(article);
    }
}