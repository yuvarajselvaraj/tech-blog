package com.techblog.blog.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techblog.blog.model.BlogPost;
import com.techblog.blog.repository.BlogPostRepository;

@Controller
public class WebController {
    private final BlogPostRepository blogPostRepository;

    public WebController(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @GetMapping(value = "/blogs", produces = "text/html")
    public String blogs(
        Model model,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        int pageIndex = Math.max(0, page);
        int pageSize = Math.max(1, Math.min(size, 10));
        Page<BlogPost> pageResult = blogPostRepository
            .findAll(PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "publishedAt", "id")));

        List<BlogPost> posts = pageResult.getContent();
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", pageResult.getNumber());
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("hasPrev", pageResult.hasPrevious());
        model.addAttribute("hasNext", pageResult.hasNext());
        model.addAttribute("size", pageSize);
        return "blogs";
    }
}

