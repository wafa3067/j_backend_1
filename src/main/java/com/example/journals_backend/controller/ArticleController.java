package com.example.journals_backend.controller;
import java.sql.Date;
import java.text.SimpleDateFormat;
import com.example.journals_backend.entity.Article;
import com.example.journals_backend.entity.User;
import com.example.journals_backend.repo.ArticleRepo;
import com.example.journals_backend.service.ArticleService;
import com.example.journals_backend.service.UserSerivce;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.journals_backend.entity.Contributors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ArticleController {

    private final ArticleService articleService;
    private final UserSerivce userSerivce;

    public ArticleController(ArticleService articleService, UserSerivce userSerivce) {
        this.articleService = articleService;
        this.userSerivce = userSerivce;
    }



    @GetMapping("/search")
    public List<Article> searchArticles(@RequestParam String keyword,
                                        @RequestParam(required = false) String publishedAfter,
                                        @RequestParam(required = false) String publishedBefore,
                                        @RequestParam(required = false) String author) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = publishedAfter != null ? new Date(dateFormat.parse(publishedAfter).getTime()) : null;
        Date endDate = publishedBefore != null ? new Date(dateFormat.parse(publishedBefore).getTime()) : null;

        return articleService.searchArticles(keyword, startDate, endDate, author);
    }

    // ✅ Create new article with file upload
    @PostMapping("/upload-form")
    public String uploadArticleFromForm(
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam("title") String title,
            @RequestParam("createdAt") String createdAt,
            @RequestParam(value = "section", required = false) String section,
            @RequestParam(value = "previousPublished", required = false) String previousPublished,
            @RequestParam(value = "fileType", required = false) String fileType,
            @RequestParam(value = "refrencedUrl", required = false) String refrencedUrl,
            @RequestParam(value = "textStyle", required = false) String textStyle,
            @RequestParam(value = "textStylic", required = false) String textStylic,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam(value = "crosspendingContact", required = false) String crosspendingContact,
            @RequestParam(value = "copyright", required = false) String copyright,
            @RequestParam(value = "privacypolicy", required = false) String privacypolicy,
            @RequestParam(value = "pdf", required = false) String pdf,
            @RequestParam(value = "prefix", required = false) String prefix,
            @RequestParam(value = "subtitle", required = false) String subtitle,
            @RequestParam(value = "abstracts", required = false) String abstracts,
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "references", required = false) String references,
            @RequestBody(required = false) List<Contributors> contributorsList
    ) throws IOException {

        Article savedArticle = articleService.saveArticle(
                title,
                pdfFile,
                section,
                previousPublished,
                fileType,
                refrencedUrl,
                textStyle,
                textStylic,
                comment,
                crosspendingContact,
                copyright,
                privacypolicy,
                createdAt,
                pdf,
                prefix,
                subtitle,
                abstracts,
                keywords,
                references,
                contributorsList

        );

        return "redirect:success";
    }


    @PostMapping("/uploadFinal")
    public ResponseEntity<Article> uploadFinalFile(
            @RequestParam("user") Long user,
            @RequestParam("finalFile") MultipartFile finalFile) throws IOException {

                    Article savedArticle = articleService.uploadFinalFile(finalFile,user);
        return ResponseEntity.ok(savedArticle);


}

    @PostMapping("/upload")
    public ResponseEntity<Article> uploadArticle(
            @RequestParam("title") String title,
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam("section") String section,
            @RequestParam("previousPublished") String previousPublished,
            @RequestParam("fileType") String fileType,
            @RequestParam("refrencedUrl") String refrencedUrl,
            @RequestParam("abstracts") String abstracts,
            @RequestParam("keywords") String keywords,
            @RequestParam("textStyle") String textStyle,
            @RequestParam("user") String user,
            @RequestParam("textStylic") String textStylic,
            @RequestParam("comment") String comment,
            @RequestParam("crosspendingContact") String crosspendingContact,
            @RequestParam("copyright") String copyright,
            @RequestParam("privacypolicy") String privacypolicy,
            @RequestParam("referenceText") String referenceText,
            @RequestParam("prefix") String prefix,
            @RequestParam("subtitle") String subtitle,
            @RequestParam("createdAt") String createdAt,
            @RequestParam(value = "approved", required = false, defaultValue = "") String approvedBy,
            @RequestParam("contributorsList") String contributorsListJson // JSON string of contributors
    ) throws IOException {

        // Parse contributors JSON into List<Contributors>
        ObjectMapper objectMapper = new ObjectMapper();
        List<Contributors> contributorsList = objectMapper.readValue(
                contributorsListJson,
                new TypeReference<List<Contributors>>() {
                }
        );

        Article savedArticle = articleService.saveArticle(
                title,
                pdfFile,
                section,
                previousPublished,
                fileType,
                refrencedUrl,
                textStyle,
                textStylic,
                comment,
                user,
                copyright,
                privacypolicy,
                approvedBy,
                createdAt,
                prefix,
                subtitle,
                abstracts,
                keywords,
                referenceText,
                contributorsList

        );
        return ResponseEntity.ok(savedArticle);

    }

    @PostMapping("/updatePdfAndComments")
    public ResponseEntity<Article> updatePdfAndComments(
            @RequestParam("articleId") Long articleId,
            @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,
            @RequestParam(value = "userComments", required = false) String userComments
    ) throws IOException {
        Article updatedArticle = articleService.updatePdfAndComments(articleId, pdfFile, userComments);
        return ResponseEntity.ok(updatedArticle);
    }


@PostMapping("/updateProduction")
    public ResponseEntity<String> updatePdfAndComments(
            @RequestParam("articleId") Long articleId,
            @RequestParam(value = "productionComments", required = false) String productionComments
    ) throws IOException
{
        var updatedArticle = articleService.updateProductionComments(productionComments,articleId);
            articleService.ModificationProduction(articleId,true);
        return ResponseEntity.ok(updatedArticle);
    }


    @GetMapping("/template")
    public ResponseEntity<String> getTemplates(){
        return  ResponseEntity.ok("http://localhost:8080/uploads/IEEE_Conference_Template.pdf");
    }

@PostMapping("/updateUserProduction")
    public ResponseEntity<String> updateUserProductionComments(
            @RequestParam("articleId") Long articleId,
            @RequestParam(value = "productionComments", required = false) String userProductionComments
    ) throws IOException
{
        var updatedArticle = articleService.updateProductionUserComments(userProductionComments,articleId);
        return ResponseEntity.ok(updatedArticle);
    }



    @PostMapping("/updateComments")
    public ResponseEntity<String> updateUserCommentS(
            @RequestParam("articleId") Long articleId,
            @RequestParam(value = "userComments", required = false) String userComments
    ) {
        var comments = articleService.updateComments(userComments, articleId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/modify_copy")
    public ResponseEntity<String> modifyCopy(
            @RequestParam("articleId") Long articleId,
            @RequestParam(value = "copy", required = false) boolean copy
    ) {
        articleService.ModificationCopy(articleId,copy);
        return ResponseEntity.ok("updated");
    }

 @PostMapping("/modify_production")
    public ResponseEntity<String> modifyProduction(
            @RequestParam("articleId") Long articleId,
            @RequestParam(value = "production", required = false) boolean production
    ) {
        articleService.ModificationProduction(articleId,production);
        return ResponseEntity.ok("updated");
    }

    // ✅ Get all articles
    @GetMapping("/get")
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }


    @GetMapping("/user/{user}")
    public ResponseEntity<List<Article>> getArticleByUser(@PathVariable String user) {
        List<Article> articles = articleService.getArticleWithUser(user);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<Optional<Article>> getArticleByUser(@PathVariable Long id) {
        Optional<Article> articles = articleService.getArticleById(id);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> articles = userSerivce.getAllUsers();
        return ResponseEntity.ok(articles);
    }


    // ✅ Delete article by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok("Article deleted successfully!");
    }


    // ✅ Total articles by user
    @GetMapping("/total-article")
    public ResponseEntity<Long> getTotalArticles(@RequestParam String email) {

        return ResponseEntity.ok(articleService.getTotal(email));
    }

    // ✅ Pending articles by user
    @GetMapping("/pending-article")
    public ResponseEntity<Long> getPendingArticles(@RequestParam String email) {
        return ResponseEntity.ok(articleService.getPending(email));
    }


    // ✅ Approved articles by user
    @GetMapping("/total-approved")
    public ResponseEntity<Long> getApprovedArticles(@RequestParam String email) {
        return ResponseEntity.ok(articleService.getApproved(email));
    }

    // ✅ Approved articles by user
    @GetMapping("/pending-articles")
    public ResponseEntity<List<Object[]>> getAllpendingArtilcles() {
        return ResponseEntity.ok(articleService.getPendingAllArticles());
    }

    // ✅ Approved articles by user
    @GetMapping("/rejected-articles")
    public ResponseEntity<List<Article>> getAllRejectArticles() {
        return ResponseEntity.ok(articleService.getAllRejectedArticles());
    }

    // ✅ Approved articles by user
    @GetMapping("/approved-articles")
    public ResponseEntity<List<Article>> getAllApproved() {
        return ResponseEntity.ok(articleService.getApprovedAllArtices());
    }

    @PostMapping("/status/{id}")
    public ResponseEntity<String> updateArticleStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String reviewer,
            @RequestParam(required = false) String reviewStartDate,
            @RequestParam(required = false) String reviewEndDate
    ) {
        try {
            articleService.updateReviewerAndStage(id, reviewer, reviewStartDate, reviewEndDate, status);
            return ResponseEntity.ok("Updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
