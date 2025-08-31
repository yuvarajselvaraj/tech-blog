package com.techblog.blog.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techblog.blog.model.BlogPost;
import com.techblog.blog.repository.BlogPostRepository;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    private final BlogPostRepository blogPostRepository;

    public BlogController(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @GetMapping(produces = "application/json")
    public List<BlogPost> listBlogs(
        @RequestParam(name = "size", defaultValue = "50") int pageSize
    ) {
        PageRequest firstPageWithSort = PageRequest.of(
            0,
            Math.max(1, Math.min(pageSize, 200)),
            Sort.by(Sort.Direction.DESC, "publishedAt", "id")
        );
        return blogPostRepository.findAll(firstPageWithSort).getContent();
    }
}

