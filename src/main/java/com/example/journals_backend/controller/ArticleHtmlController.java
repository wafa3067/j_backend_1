package com.example.journals_backend.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ArticleHtmlController {

    @GetMapping("/article_form")
    public String showUploadForm() {
        return "article_form";
    }

}
