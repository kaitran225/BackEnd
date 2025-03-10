package com.healthy.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthy.backend.entity.Tags;

@Repository
public interface TagsRepository extends JpaRepository<Tags, String> {

    @Query("SELECT t.tagId FROM Tags t ORDER BY t.tagId DESC LIMIT 1")
    String findLastTagId();

    Optional<Tags> findByTagName(String tagName);

    boolean existsByTagName(String tagName);

    @Query("SELECT t FROM Tags t WHERE UPPER(t.tagName) LIKE UPPER(CONCAT('%', :keyword, '%')) ")
    List<Tags> findKeyWordInTag(@Param("keyword") String keyword);

    List<Tags> findAll();
}
