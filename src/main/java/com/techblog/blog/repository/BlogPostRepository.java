package com.techblog.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techblog.blog.model.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    boolean existsByGuid(String guid);
}

