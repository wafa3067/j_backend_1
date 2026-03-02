package com.example.journals_backend.admin.admincontroller;


import com.example.journals_backend.admin.adminservice.AdminLoginService;
import com.example.journals_backend.dto.CopyEditorDto;
import com.example.journals_backend.dto.PenddingDto;
import com.example.journals_backend.dto.ProductionDto;
import com.example.journals_backend.entity.Article;
import com.example.journals_backend.entity.User;
import com.example.journals_backend.repo.ArticleRepo;
import com.example.journals_backend.service.ArticleService;
import com.example.journals_backend.service.UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminPageRestController {

    private final ArticleService articleService;
    private final UserSerivce userSerivce;
    private final ArticleRepo articleRepo;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AdminLoginService adminService;

    public AdminPageRestController(ArticleService articleService, UserSerivce userSerivce, ArticleRepo articleRepo) {
        this.articleService = articleService;
        this.userSerivce = userSerivce;
        this.articleRepo = articleRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestParam String username, @RequestParam String password) {
        String url = "http://localhost:8080/admin/login";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", username);
        body.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            Map<String, String> response = restTemplate.postForObject(url, requestEntity, Map.class);
            if (response.containsKey("error")) {
                return ResponseEntity.badRequest().body(Map.of("error", response.get("error")));
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Login failed ❌"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> handleRegister(@RequestParam String username, @RequestParam String password) {
        adminService.registerAdmin(username, password);
        return ResponseEntity.ok(Map.of("message", "Admin registered successfully!"));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PenddingDto>> getPendingArticles() {
        return ResponseEntity.ok(articleService.getPendingArticles());
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<PenddingDto>> getRejectedSqlController() {
        return ResponseEntity.ok(articleService.getRejectedSql());
    }



    @GetMapping("/approved")
    public ResponseEntity<List<PenddingDto>> getApprovedSqlController() {
        return ResponseEntity.ok(articleService.getApprovedSql());
    }

    @GetMapping("/under-review")
    public ResponseEntity<List<PenddingDto>> getUnderReview() {
        return ResponseEntity.ok(articleService.getUnderReviewArticles());
    }

    @GetMapping("/copy-editor")
    public ResponseEntity<List<CopyEditorDto>> getCopyEditor() {
        return ResponseEntity.ok(articleService.getCopyEditor());
    }

    @GetMapping("/get-production")
    public ResponseEntity<List<ProductionDto>> getProductionController() {
        return ResponseEntity.ok(articleService.getProduction());
    }

    @PostMapping("/review/{id}")
    public ResponseEntity<?> reviewDecision(@PathVariable Long id, @RequestParam String decision, @RequestParam(required = false) String comment) {
        Article article = articleRepo.findById(id).orElse(null);
        if (article != null) {
            article.setReviewerComment(comment);
            article.setStatus(decision.equals("approve") ? "approved" : "rejected");
            articleRepo.save(article);
            return ResponseEntity.ok(Map.of("message", "Article reviewed successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Article not found"));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userSerivce.getAllUsers());
    }

    @PostMapping("/users/status/{id}")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestParam String status) {
        userSerivce.updateStatus(id, status);
        return ResponseEntity.ok(Map.of("message", "User status updated successfully"));
    }

    @PostMapping("/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userSerivce.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @PostMapping("/articles/status/{id}")
    public ResponseEntity<?> updateArticleStatus(@PathVariable Long id, @RequestParam String status, @RequestParam(required = false) String reviewerAssigned, @RequestParam(required = false) String reviewStartDate, @RequestParam(required = false) String reviewEndDate
    ) {

        var emailResponse = "";

        // Validate required fields
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Status is required"));
        }

        // If status is "under review", reviewerAssigned must be provided
        if ("under review".equalsIgnoreCase(status) && (reviewerAssigned == null || reviewerAssigned.isBlank())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Reviewer must be assigned for 'under review' status"));
        }

        // Validate date formats
        try {
            if (reviewStartDate != null) LocalDate.parse(reviewStartDate);
            if (reviewEndDate != null) LocalDate.parse(reviewEndDate);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format. Use yyyy-MM-dd"));
        }

        try {
            // Update article in DB
            articleService.updateReviewerAndStage(id, reviewerAssigned, reviewStartDate, reviewEndDate, status);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to update article: " + e.getMessage()));
        }

        return ResponseEntity.ok(Map.of("Article updated successfully", emailResponse));
    }

    @PostMapping("/articles/copyeditor/{id}")
    public ResponseEntity<?> uodateCopyEditorStatus(@PathVariable Long id,
                                                    @RequestParam String status,
                                                    @RequestParam String comments,
                                                    @RequestParam(required = false) String copyEditor
    ) {

        try {

            articleService.updateCopyEditor(id, copyEditor, status,comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to update article: " + e.getMessage()));
        }

        return ResponseEntity.ok(Map.of("message", "Article updated successfully"));
    }

    @PostMapping("/articles/production/{id}")
    public ResponseEntity<?> updateProduction(@PathVariable Long id, @RequestParam String status, @RequestParam(required = false) String productionNotes
) {
        try {
            // Update article in DB
            articleService.updateProduction(id, productionNotes, status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to update article: " + e.getMessage()));
        }

        return ResponseEntity.ok(Map.of("message", "Article updated successfully"));
    }

    @PostMapping("/articles/reject/{id}")
    public ResponseEntity<?> rejectArticle(@PathVariable Long id, @RequestParam String status, @RequestParam(required = false) String comments
) {

        try {

            articleService.updateRevierComments(id, comments, status);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to update article: " + e.getMessage()));
        }

        return ResponseEntity.ok(Map.of("message", "Article updated successfully"));
    }


    @PostMapping("/articles/modify-copy/{id}")
    public ResponseEntity<?> modifyArticleCopy(@PathVariable Long id, @RequestParam boolean copy){

        try {

            articleService.ModificationCopy(id, copy);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to update article: " + e.getMessage()));
        }

        return ResponseEntity.ok(Map.of("message", "Article updated successfully"));

    } @PostMapping("/articles/modify-production/{id}")
    public ResponseEntity<?> modifyArticleProdiction(@PathVariable Long id, @RequestParam boolean copy){

        try {

            articleService.ModificationProduction(id, copy);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to update article: " + e.getMessage()));
        }

        return ResponseEntity.ok(Map.of("message", "Article updated successfully"));

    }

    @PostMapping("/email")
    public ResponseEntity<Map<String, String>> sendingMail(@RequestParam String toEmail, @RequestParam String authorName, @RequestParam String status, @RequestParam String articleTitle,@RequestParam String body) {
        try {
            userSerivce.sendMail(toEmail, authorName, articleTitle, status,body);
            return ResponseEntity.ok(Map.of("message", "Email sent successfully"));
        } catch (Exception e) {
            e.printStackTrace(); // optional: log exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to send email: " + e.getMessage()));
        }
    }


}
