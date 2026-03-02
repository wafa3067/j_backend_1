package com.example.journals_backend.admin.admincontroller;


import com.example.journals_backend.admin.AdminLogin;
import com.example.journals_backend.admin.adminservice.AdminLoginService;
import com.example.journals_backend.dto.PenddingDto;
import com.example.journals_backend.entity.Article;
import com.example.journals_backend.repo.ArticleRepo;
import com.example.journals_backend.service.ArticleService;
import com.example.journals_backend.service.UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/controller")
public class AdminPageController {

    private final ArticleService  articleService;
    private final UserSerivce userSerivce;
    private final ArticleRepo  articleRepo;

    private final RestTemplate restTemplate = new RestTemplate();

    public AdminPageController(ArticleService articleService, UserSerivce userSerivce, ArticleRepo articleRepo) {
        this.articleService = articleService;
        this.userSerivce = userSerivce;
        this.articleRepo = articleRepo;
    }


    @GetMapping("/login-page")
    public String showLoginPage() {
        return "login"; // templates/login.html
    }

    @PostMapping("/login-page")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {

        String url = "http://localhost:8080/admin/login";

        // Create a request body with username and password
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", username);
        body.add("password", password);

        // Set headers for form data
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> response = restTemplate.postForObject(url, requestEntity, Map.class);

            if (response.containsKey("error")) {
                model.addAttribute("error", response.get("error"));
                return "login";
            }

            model.addAttribute("username", username);
            model.addAttribute("token", response.get("token"));
            return "dashboard";

        } catch (Exception e) {
            model.addAttribute("error", "Login failed ❌");
            return "login";
        }
    }


    @GetMapping("/register-html-page")
    public String showRegisterPage() {
        return "register"; // templates/register.html.html
    }

    @Autowired
    private AdminLoginService adminService;
    // Handle Form Submission
    @PostMapping("/register-html-page")
    public String handleRegister(@RequestParam String username,
                                 @RequestParam String password,
                                 Model model) {

        // Call your AdminService directly — not via RestTemplate
        AdminLogin admin = new AdminLogin();
        admin.setEmail(username);
        admin.setPassword(password);

        // Save admin (you can autowire AdminService here)
        adminService.registerAdmin(username,password);

        model.addAttribute("message", "Admin registered successfully!");
        return "login"; // Show login page
    }


    @GetMapping("/pendding")
    public String pendding(Model model) {
        model.addAttribute("pending", articleService.getPendingArticles());
        return "pending";
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PenddingDto>> getPendingArticles() {
        return ResponseEntity.ok(articleService.getPendingArticles());
    }


    @PostMapping("/review/{id}")
    public String reviewDecision(@PathVariable Long id,
                                 @RequestParam String decision,
                                 @RequestParam(required = false) String comment) {
        Article article = articleRepo.findById(id).orElse(null);
        if (article != null) {
            article.setReviewerComment(comment);
            article.setStatus(decision.equals("approve") ? "approved" : "rejected");
            articleRepo.save(article);
        }
        return "redirect:/admin/pending";
    }


    @GetMapping("/setting")
    public String setting(Model model) {
//        model.addAttribute("approved", articleService.getApprovedArticles());
        return "setting";
    }
    @GetMapping("/approved")
    public String approved(Model model) {
        model.addAttribute("approved", articleService.getPendingArticles());
        return "approved";
    }

    @GetMapping("/rejected")
    public String rejected(Model model) {
        model.addAttribute("rejected",  articleService.getPendingArticles());
        return "rejected";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userSerivce.getAllUsers());
        return "users";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
//        articleService.approveArticle(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id) {
//        articleService.rejectArticle(id, "Rejected by admin");
        return "redirect:/admin/dashboard";
    }


    @PostMapping("/users/status/{id}")
    public String updateUserStatus(@PathVariable Long id, @RequestParam String status) {
        userSerivce.updateStatus(id, status);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userSerivce.deleteUser(id);
        return "redirect:/admin/users";
    }


    @PostMapping("/articles/status/{id}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String reviewerAssigned,
            @RequestParam(required = false) String reviewStartDate,
            @RequestParam(required = false) String reviewEndDate) {

        articleService.updateReviewerAndStage(id,  reviewerAssigned, reviewStartDate, reviewEndDate,status);
        return ResponseEntity.ok().build();
    }

}
