package com.healthy.backend.controller;

import com.healthy.backend.dto.article.ArticleRequest;
import com.healthy.backend.dto.article.ArticleResponse;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/articles")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Article Controller", description = "Article related APIs.")
public class ArticleController {

    private final ArticleService articleService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/createArticle")
    @Operation(
            deprecated = true,
            summary = "Create a new article",
            description = "Creates a new article."
    )
    public ResponseEntity<?> createArticle(
            HttpServletRequest requestUser,
            @RequestBody @Valid ArticleRequest article) {
        try {
            articleService.createArticle(requestUser, article);
            return ResponseEntity.ok("Article created successfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the article" + ex.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            hidden = true,
            summary = "Test article",
            description = "Test article.")
    @PostMapping("/test-article")
    public ResponseEntity<ArticleRequest> testArticle(@RequestBody ArticleRequest article) {
        System.out.println(article.getTitle());
        System.out.println(article.getTags());
        return ResponseEntity.ok(article);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/all")
    @Operation(

            summary = "Get all articles",
            description = "Returns a list of all articles."
    )
    public ResponseEntity<List<ArticleResponse>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("")
    @Operation(

            summary = "Get an article by ID",
            description = "Returns an article by ID."
    )
    public ResponseEntity<ArticleResponse> getArticleById(@RequestParam String articleId) {
        return ResponseEntity.ok(articleService.getArticleById(articleId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("updateArticle")
    @Operation(

            summary = "Update an article",
            description = "Updates an existing article."
    )
    public ResponseEntity<?> updateArticle(@RequestParam String articleId, HttpServletRequest requestUser, @RequestBody ArticleRequest request) {


        try {
            articleService.updateArticle(articleId, requestUser, request);
            return ResponseEntity.ok("Article updated successfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the article" + ex.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("")
    @Operation(

            summary = "Delete an article",
            description = "Deletes an existing article."
    )
    public ResponseEntity<Void> deleteArticle(HttpServletRequest requestUser, @RequestParam String articleId) {
        articleService.deleteArticle(requestUser, articleId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/searchKeyWordTiltle")
    @Operation(

            summary = "Get articles by category",
            description = "Returns a list of articles by category."
    )
    public ResponseEntity<List<ArticleResponse>> searchArticles(@RequestParam String keyword) {
        return ResponseEntity.ok(articleService.searchArticles(keyword));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/searchKeyWordTag")
    @Operation(

            summary = "Get articles by tag",
            description = "Returns a list of articles by tag."
    )
    public ResponseEntity<List<ArticleResponse>> getArticlesByTag(@RequestParam String keyword) {
        return ResponseEntity.ok(articleService.getArticlesByTag(keyword));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/author")
    @Operation(

            summary = "Get articles by author ID",
            description = "Returns a list of articles by author ID."
    )
    public ResponseEntity<List<ArticleResponse>> getArticlesByAuthor(@RequestParam String authorId) {
        return ResponseEntity.ok(articleService.getArticlesByAuthor(authorId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/like")
    @Operation(

            summary = "Like an article",
            description = "Likes an article."
    )
    public ResponseEntity<Void> likeArticle(@RequestParam String articleId) {
        articleService.likeArticle(articleId);
        return ResponseEntity.ok().build();
    }
}