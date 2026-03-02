package com.example.journals_backend.service;

import com.example.journals_backend.dto.CopyEditorDto;
import com.example.journals_backend.dto.PenddingDto;
import com.example.journals_backend.dto.ProductionDto;
import com.example.journals_backend.entity.Article;
import com.example.journals_backend.entity.Contributors;
import com.example.journals_backend.repo.ArticleRepo;
import com.example.journals_backend.utils.DOIUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//
//@Service
//public class ArticleService {
//
//    @Value("${file.upload-dir}")
//     String uploadDir;
//
//     final ArticleRepo articleRepository;
//
//    public ArticleService(ArticleRepo articleRepository) {
//        this.articleRepository = articleRepository;
//    }
//
//    public Article saveArticle(
//            String title,
//            String authors,
//            MultipartFile pdfFile,
//             String section,
//             String previousPublished,
//             String fileType,
//             String refrencedUrl,
//             String textStyle,
//             String textStylic,
//             String comment,
//             String crosspendingContact,
//             String copyright,
//             String privacypolicy,
//             String pdf,
//             String prefix,
//             String subtitle,
//             String abstracts,
//             String keywords,
//             String references,
//             List<Contributors> contributorsList
//
//                               ) throws IOException {
//        // ensure upload folder exists
//        File folder = new File(uploadDir);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//
//        // store file on disk
//        String filePath = uploadDir + File.separator + pdfFile.getOriginalFilename();
//        pdfFile.transferTo(new File(filePath));
//
//        // save record to DB
//        Article article = new Article();
//        article.setTitle(title);
//        article.setAuthors(authors);
//        article.setPdf(filePath);
//        return articleRepository.save(article);
//    }
//}




import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ArticleService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ArticleRepo articleRepository;

    public ArticleService(ArticleRepo articleRepository) {

        this.articleRepository = articleRepository;
    }
    // Method to search articles by keyword
    // Method to search articles with filters
    public List<Article> searchArticles(String keyword, Date startDate, Date endDate, String author) {
        // Convert java.util.Date to java.sql.Date
        Date sqlStartDate = startDate != null ? new Date(startDate.getTime()) : null;
        Date sqlEndDate = endDate != null ? new Date(endDate.getTime()) : null;
        return articleRepository.searchArticles(keyword, sqlStartDate, sqlEndDate, author);
    }

    // Custom query to search by title, subtitle, and abstract


//    @Transactional
//    public Article saveArticle(
//            String title,
//            MultipartFile pdfFile,
//            String section,
//            String previousPublished,
//            String fileType,
//            String refrencedUrl,
//            String textStyle,
//            String textStylic,
//            String comment,
//            String user,
//            String copyright,
//            String privacypolicy,
//            String approvedBy,
//            String createdAt,
//            String prefix,
//            String subtitle,
//            String abstracts,
//            String keywords,
//            String references,
//            List<Contributors> contributorsList
//    ) throws IOException {
//
//        // ✅ Ensure upload directory exists
//        Path uploadPath = Paths.get(uploadDir);
//        if (Files.notExists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//        // ✅ Handle file upload
//        String originalFileName = pdfFile.getOriginalFilename();
//        if (originalFileName == null || originalFileName.isBlank()) {
//            throw new IOException("Invalid file: no name provided.");
//        }
//
//        Path filePath = uploadPath.resolve(originalFileName);
//        pdfFile.transferTo(filePath.toFile());
//
//        // ✅ Create and populate Article entity
//        Article article = new Article();
//        article.setTitle(title);
//        article.setSection(section);
//        article.setPreviousPublished(previousPublished);
//        article.setFileType(fileType);
//        article.setRefrencedUrl(refrencedUrl);
//        article.setTextStyle(textStyle);
//        article.setTextStylic(textStylic);
//        article.setComment(comment);
//        article.setStatus("pending");
//        article.setApprovedBy("pennding");
//        article.setCreatedAt(createdAt);
//        article.setUser(user);
//        article.setCopyright(copyright);
//        article.setPrivacypolicy(privacypolicy);
//        article.setPdf(filePath.toString()); // saved file path
//        article.setPrefix(prefix);
//        article.setSubtitle(subtitle);
//        article.setAbstracts(abstracts);
//        article.setKeywords(keywords);
//        article.setReferenceText(references);
//        article.setContributorsList(contributorsList);
//        String doi = DOIUtil.generateDOI(article.getId());
//        article.setDoi(doi);
//        article.setModifyCopyEditor(false);
//        article.setModifyProduction(false);
//        // ✅ Save article to DB
//        return articleRepository.save(article);
//    }

@Transactional
public Article uploadFinalFile(MultipartFile finalFile,Long user) throws IOException {
    Article article = articleRepository.findById(user)
            .orElseThrow(() -> new RuntimeException("Article not found with ID: " + user));

    String pdfUrl = uploadPdfToStorage(finalFile); // Assume this method uploads the file and returns the URL

    article.setFinalFile(pdfUrl); // Save the file URL
    return articleRepository.save(article);


}

@Transactional
public Article saveArticle(
        String title,
        MultipartFile pdfFile,
        String section,
        String previousPublished,
        String fileType,
        String refrencedUrl,
        String textStyle,
        String textStylic,
        String comment,
        String user,
        String copyright,
        String privacypolicy,
        String approvedBy,
        String createdAt,
        String prefix,
        String subtitle,
        String abstracts,
        String keywords,
        String references,
        List<Contributors> contributorsList
) throws IOException {
    // Handle file upload (e.g., to AWS S3)
    String pdfUrl = uploadPdfToStorage(pdfFile); // Assume this method uploads the file and returns the URL

    // Create and populate Article entity
    Article article = new Article();
    article.setPdfUrl(pdfUrl); // Save the file URL
    article.setTitle(title);
    article.setSection(section);
    article.setPreviousPublished(previousPublished);
    article.setFileType(fileType);
    article.setRefrencedUrl(refrencedUrl);
    article.setTextStyle(textStyle);
    article.setTextStylic(textStylic);
    article.setComment(comment);
    article.setStatus("pending");
    article.setApprovedBy("pending");
    article.setCreatedAt(createdAt);
    article.setUser(user);
    article.setCopyright(copyright);
    article.setPrivacypolicy(privacypolicy);
    article.setPrefix(prefix);
    article.setSubtitle(subtitle);
    article.setAbstracts(abstracts);
    article.setKeywords(keywords);
    article.setReferenceText(references);
    article.setContributorsList(contributorsList);

    // Save article to DB
    return articleRepository.save(article);
}

//for local database
//    private String uploadPdfToStorage(MultipartFile pdfFile) throws IOException {
//        // Define the path where the file will be saved
//        String uploadDirectory = "/Users/wafaabbas/Documents/spring/journals_backend/uploads/"; // Correct directory path
//        String fileName = pdfFile.getOriginalFilename();
//
//        // Ensure the directory exists
//        File directory = new File(uploadDirectory);
//        if (!directory.exists()) {
//            directory.mkdirs();  // Create the directory if it doesn't exist
//        }
//
//        // Create a path for the file to be saved
//        File file = new File(uploadDirectory + fileName);
//        pdfFile.transferTo(file);  // Save the file to the local file system
//
//        // Return the URL (e.g., http://localhost:8080/uploads/filename.pdf)
//        return "http://localhost:8080/uploads/" + fileName;
//    }

//for ec2 aws
private String uploadPdfToStorage(MultipartFile pdfFile) throws IOException {
    // 1. Use a relative path so it works on any OS
    String uploadDirectory = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
    String fileName = pdfFile.getOriginalFilename();

    // 2. Ensure the directory exists
    File directory = new File(uploadDirectory);
    if (!directory.exists()) {
        directory.mkdirs();
    }

    // 3. Save the file
    File file = new File(uploadDirectory + fileName);
    pdfFile.transferTo(file);

    // 4. Return a dynamic URL (Hardcoding localhost will break in production)
    // It's better to return just the filename or a relative path
    return "/uploads/" + fileName;
}
//    @Transactional
//    public Article saveArticle(
//            String title,
//            MultipartFile pdfFile,
//            String section,
//            String previousPublished,
//            String fileType,
//            String refrencedUrl,
//            String textStyle,
//            String textStylic,
//            String comment,
//            String user,
//            String copyright,
//            String privacypolicy,
//            String approvedBy,
//            String createdAt,
//            String prefix,
//            String subtitle,
//            String abstracts,
//            String keywords,
//            String references,
//            List<Contributors> contributorsList
//    ) throws IOException {
//
//        // Create and populate Article entity
//        Article article = new Article();
//        article.setTitle(title);
//        article.setSection(section);
//        article.setPreviousPublished(previousPublished);
//        article.setFileType(fileType);
//        article.setRefrencedUrl(refrencedUrl);
//        article.setTextStyle(textStyle);
//        article.setTextStylic(textStylic);
//        article.setComment(comment);
//        article.setStatus("pending");
//        article.setApprovedBy("pending");
//        article.setCreatedAt(createdAt);
//        article.setUser(user);
//        article.setCopyright(copyright);
//        article.setPrivacypolicy(privacypolicy);
//        article.setPrefix(prefix);
//        article.setSubtitle(subtitle);
//        article.setAbstracts(abstracts);
//        article.setKeywords(keywords);
//        article.setReferenceText(references);
//        article.setContributorsList(contributorsList);
//
//        // Convert PDF file to byte[] and store it in the database
//        if (pdfFile != null && !pdfFile.isEmpty()) {
//            article.setPdfData(pdfFile.getBytes());  // Store PDF as binary data
//            article.setPdfFileName(pdfFile.getOriginalFilename()); // Store file name
//            article.setPdfContentType(pdfFile.getContentType()); // Store MIME type
//        }
//
//        // Generate DOI (if needed)
//        String doi = DOIUtil.generateDOI(article.getId());
//        article.setDoi(doi);
//
//        article.setModifyCopyEditor(false);
//        article.setModifyProduction(false);
//
//        // Save article to DB
//        return articleRepository.save(article);
//    }


    public Article updatePdfAndComments(Long articleId, MultipartFile pdfFile, String userComments) throws IOException {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found with ID: " + articleId));

        // ✅ Update PDF file if provided
        if (pdfFile != null && !pdfFile.isEmpty()) {
            // Ensure upload directory exists
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Unique file name (timestamp + original name)
            String originalFilename = pdfFile.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFilename;

            Path filePath = Paths.get(uploadDir + File.separator + fileName);
            Files.copy(pdfFile.getInputStream(), filePath);

            // Save only the path in DB
            article.setPdf(filePath.toString());

        }

        // ✅ Update user comments if provided
        if (userComments != null && !userComments.trim().isEmpty()) {
            article.setUserComments(userComments);
        }

        return articleRepository.save(article);
    }

    public String updateComments(String userComments,Long articleId){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found with ID: " + articleId));
            article.setUserComments(userComments);
        articleRepository.save(article);
        return "User Comments update successfully";
    }
    public String updateProductionUserComments(String productionUserComments,Long articleId){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found with ID: " + articleId));
            article.setUserProductionComments(productionUserComments);
        articleRepository.save(article);
        return "User Comments update successfully";
    }


    public String updateProductionComments(String production,Long articleId){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found with ID: " + articleId));
            article.setProductionComments(production);

        articleRepository.save(article);
        return "User Comments update successfully";
    }


    // Optional: Fetch all articles
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public List<Article> getArticleWithUser(String id){
      return  articleRepository.findByUser(id);
    }

    public Optional<Article> getArticleById(Long id){
      return  articleRepository.findById(id);
    }

    // Optional: Delete an article
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }


    public Long getTotal(String email) {
        return articleRepository.countTotalArticlesByUser(email);
    }


    public Long getPending( String email) {
       return articleRepository.countPendingArticlesByUser(email);

    }

    // ✅ Approved articles by user

    public Long getApproved( String email) {
        return articleRepository.countApprovedArticlesByUser(email);

    }

    public List<Object[]>  getPendingAllArticles(){
      return  articleRepository.getPendingArticles();
    }
    public List<Article>  getApprovedAllArtices(){
      return  articleRepository.getApprovedArticles();
    }
    public List<Article>  getAllRejectedArticles(){
      return  articleRepository.getRejectedArticles();
    }

    public void setReviewerComment(Long id, String comment, String status) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        article.setReviewerComment(comment);
        article.setStatus(status);
        articleRepository.save(article);
    }

    public List<PenddingDto> getPendingArticles() {
        List<Object[]> rows = articleRepository.getPendingArticles();
        List<PenddingDto> list = new ArrayList<>();

        for (Object[] r : rows) {
            PenddingDto dto = new PenddingDto(
                    ((Number) r[0]).longValue(),
                    (String) r[1],
                    (String) r[2],
                    (String) r[3],
                    (String) r[4],
                    (String) r[5],
                    (String) r[6],
                    (String) r[7],
                    (String) r[8],
                    (String) r[9],
                    (String) r[10],
                    (String) r[11],
                    (String) r[12],
                    (String) r[13],
                    (String) r[14],
                    (String) r[15],
                    (String) r[16],
                    (String) r[17],
                    (String) r[18],
                    (String) r[19],
                    (String) r[20], // reviewerAssigned
                    (String) r[21], // reviewStartDate
                    (String) r[22], // reviewEndDate
                    (String) r[23], // copyeditor
                    (String) r[24]  // stage
            );
            list.add(dto);
        }

        return list;
    }

    public List<PenddingDto> getRejectedSql() {
        List<Object[]> rows = articleRepository.getRejectedSql();
        List<PenddingDto> list = new ArrayList<>();

        for (Object[] r : rows) {
            PenddingDto dto = new PenddingDto(
                    ((Number) r[0]).longValue(),
                    (String) r[1],
                    (String) r[2],
                    (String) r[3],
                    (String) r[4],
                    (String) r[5],
                    (String) r[6],
                    (String) r[7],
                    (String) r[8],
                    (String) r[9],
                    (String) r[10],
                    (String) r[11],
                    (String) r[12],
                    (String) r[13],
                    (String) r[14],
                    (String) r[15],
                    (String) r[16],
                    (String) r[17],
                    (String) r[18],
                    (String) r[19],
                    (String) r[20], // reviewerAssigned
                    (String) r[21], // reviewStartDate
                    (String) r[22], // reviewEndDate
                    (String) r[23], // copyeditor
                    (String) r[24]  // stage
            );
            list.add(dto);
        }

        return list;
    }

    public List<PenddingDto> getApprovedSql() {
        List<Object[]> rows = articleRepository.getApprovedSql();
        List<PenddingDto> list = new ArrayList<>();

        for (Object[] r : rows) {
            PenddingDto dto = new PenddingDto(
                    ((Number) r[0]).longValue(),
                    (String) r[1],
                    (String) r[2],
                    (String) r[3],
                    (String) r[4],
                    (String) r[5],
                    (String) r[6],
                    (String) r[7],
                    (String) r[8],
                    (String) r[9],
                    (String) r[10],
                    (String) r[11],
                    (String) r[12],
                    (String) r[13],
                    (String) r[14],
                    (String) r[15],
                    (String) r[16],
                    (String) r[17],
                    (String) r[18],
                    (String) r[19],
                    (String) r[20], // reviewerAssigned
                    (String) r[21], // reviewStartDate
                    (String) r[22], // reviewEndDate
                    (String) r[23], // copyeditor
                    (String) r[24]  // stage
            );
            list.add(dto);
        }

        return list;
    }

    public List<PenddingDto> getUnderReviewArticles() {
        List<Object[]> rows = articleRepository.getUnderReview();
        List<PenddingDto> list = new ArrayList<>();

        for (Object[] r : rows) {
            PenddingDto dto = new PenddingDto(
                    ((Number) r[0]).longValue(),
                    (String) r[1],
                    (String) r[2],
                    (String) r[3],
                    (String) r[4],
                    (String) r[5],
                    (String) r[6],
                    (String) r[7],
                    (String) r[8],
                    (String) r[9],
                    (String) r[10],
                    (String) r[11],
                    (String) r[12],
                    (String) r[13],
                    (String) r[14],
                    (String) r[15],
                    (String) r[16],
                    (String) r[17],
                    (String) r[18],
                    (String) r[19],
                    (String) r[20], // reviewerAssigned
                    (String) r[21], // reviewStartDate
                    (String) r[22], // reviewEndDate
                    (String) r[23], // copyeditor
                    (String) r[24]  // stage
            );
            list.add(dto);
        }

        return list;
    }
    public List<CopyEditorDto> getCopyEditor() {
        List<Object[]> rows = articleRepository.getCopyEditor();
        List<CopyEditorDto> list = new ArrayList<>();
        for (Object[] r : rows) {
            CopyEditorDto dto = new CopyEditorDto(
                    ((Number) r[0]).longValue(),
                    (String) r[1],
                    (String) r[2],
                    (String) r[3],
                    (String) r[4],
                    (String) r[5],
                    (String) r[6],
                    (String) r[7],
                    (String) r[8],
                    (String) r[9],
                    (String) r[10],
                    (String) r[11],
                    (String) r[12],
                    (String) r[13],
                    (String) r[14],
                    (String) r[15],
                    (String) r[16],
                    (String) r[17],
                    (String) r[18],
                    (String) r[19],
                    (String) r[20], // reviewerAssigned
                    (String) r[21], // reviewStartDate
                    (String) r[22], // reviewEndDate
                    (String) r[23], // copyeditor
                    (String) r[24],
                    (String) r[25] , // stage
                    (String) r[26],  // stage
                    (Boolean) r[27]  // stage
            );
            list.add(dto);
        }

        return list;
    }

    public List<ProductionDto> getProduction() {
        List<Object[]> rows = articleRepository.getProduction();
        List<ProductionDto> list = new ArrayList<>();

        for (Object[] r : rows) {
            ProductionDto dto = new ProductionDto(
                    ((Number) r[0]).longValue(),
                    (String) r[1],
                    (String) r[2],
                    (String) r[3],
                    (String) r[4],
                    (String) r[5],
                    (String) r[6],
                    (String) r[7],
                    (String) r[8],
                    (String) r[9],
                    (String) r[10],
                    (String) r[11],
                    (String) r[12],
                    (String) r[13],
                    (String) r[14],
                    (String) r[15],
                    (String) r[16],
                    (String) r[17],
                    (String) r[18],
                    (String) r[19],
                    (String) r[20], // reviewerAssigned
                    (String) r[21], // reviewStartDate
                    (String) r[22], // reviewEndDate
                    (String) r[23], // copyeditor
                    (String) r[24],  // stage
                    (String) r[25],
                    (String) r[26],
                    (boolean) r[27],
                    (String) r[28]
                    // stage
            );
            list.add(dto);
        }

        return list;
    }

    @Transactional
    public void updateReviewerAndStage(Long articleId, String reviewer, String startDate, String endDate, String stage) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        if(reviewer != null) article.setReviewerAssigned(reviewer);
        if(startDate != null) article.setReviewStartDate(LocalDate.parse(startDate).toString());
        if(endDate != null) article.setReviewEndDate(LocalDate.parse(endDate).toString());
        if(stage != null) article.setStage(stage);article.setStatus(stage); // update status to match front-end
    }

    @Transactional
    public void updateCopyEditor(Long articleId, String copyEditor,  String stage,String comments) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        if(copyEditor != null) article.setCopyeditor(copyEditor);
        if(stage != null) article.setStatus(stage); // update status to match front-end
        if(comments != null) article.setUnderReviewComments(comments); // update status to match front-end
    }

    @Transactional
    public void updateProduction(Long articleId, String productionNotes,  String stage) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        if(productionNotes != null) article.setProductionNotes(productionNotes);
        if(stage != null) article.setStage(stage);article.setStatus(stage); // update status to match front-end
    }

    @Transactional
    public void updateRevierComments(Long articleId, String revierComments,  String stage) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        if(revierComments != null) article.setReviewerComment(revierComments);
        if(stage != null) article.setStage(stage);article.setStatus(stage); // update status to match front-end
    }

    @Transactional
    public void ModificationCopy(Long articleId, Boolean copy) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        if(copy != null) article.setModifyCopyEditor(copy);


    }
    @Transactional
    public void ModificationProduction(Long articleId, Boolean production) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        if(production != null) article.setModifyProduction(production);
        articleRepository.save(article);
    }

}
