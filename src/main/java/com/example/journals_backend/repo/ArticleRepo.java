package com.example.journals_backend.repo;

import com.example.journals_backend.entity.Article;
import com.example.journals_backend.entity.Contributors;
import com.example.journals_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ArticleRepo extends JpaRepository<Article, Long> {

    List<Article> findByUser(String user);
//    List<Article> findArticleById(Long id);



    @Query("SELECT a FROM Article a WHERE " +
            "(a.title LIKE %?1% OR a.subtitle LIKE %?1% OR a.abstracts LIKE %?1%) " +
            "AND (:startDate IS NULL OR a.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR a.createdAt <= :endDate) "
        )
    List<Article> searchArticles(String keyword, Date startDate, Date endDate, String author);


    @Query("SELECT COUNT(a) FROM Article a WHERE a.user = :email")
    long countTotalArticlesByUser(String email);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.user = :email AND a.status = 'pending'")
    long countPendingArticlesByUser(String email);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.user = :email AND a.status = 'approved'")
    long countApprovedArticlesByUser(String email);


    @Query(value = """
                SELECT
                                                          a.id,
                                                          a.reviewer_comment,
                                                          a.section,
                                                          a.comment,
                                                          a.pdf,
                                                          a.status,
                                                          a.user,
                                                          a.approved_by,
                                                          a.prefix,
                                                          a.title,
                                                          a.subtitle,
                                                          a.abstracts,
                                                          a.keywords,
                                                          a.reference_text,
                                                          a.created_at,
                                                          u.given_name,
                                                          u.family_name,
                                                          u.email,
                                                          u.affiliation,
                                                          u.country,
                                                          a.reviewer_assigned,
                                                          a.review_start_date,
                                                          a.review_end_date,
                                                          a.copyeditor,
                                                          a.stage
                                                      FROM article a
                                                      JOIN user u ON a.user = u.email
                                                      WHERE a.status = 'pending'
            
            """, nativeQuery = true)
    List<Object[]> getPendingArticles();
    @Query(value = """
                SELECT
                                                          a.id,
                                                          a.reviewer_comment,
                                                          a.section,
                                                          a.comment,
                                                          a.pdf,
                                                          a.status,
                                                          a.user,
                                                          a.approved_by,
                                                          a.prefix,
                                                          a.title,
                                                          a.subtitle,
                                                          a.abstracts,
                                                          a.keywords,
                                                          a.reference_text,
                                                          a.created_at,
                                                          u.given_name,
                                                          u.family_name,
                                                          u.email,
                                                          u.affiliation,
                                                          u.country,
                                                          a.reviewer_assigned,
                                                          a.review_start_date,
                                                          a.review_end_date,
                                                          a.copyeditor,
                                                          a.stage
                                                      FROM article a
                                                      JOIN user u ON a.user = u.email
                                                      WHERE a.status = 'Rejected'
            
            """, nativeQuery = true)
    List<Object[]> getRejectedSql();

    @Query(value = """
                SELECT
                                                          a.id,
                                                          a.reviewer_comment,
                                                          a.section,
                                                          a.comment,
                                                          a.pdf,
                                                          a.status,
                                                          a.user,
                                                          a.approved_by,
                                                          a.prefix,
                                                          a.title,
                                                          a.subtitle,
                                                          a.abstracts,
                                                          a.keywords,
                                                          a.reference_text,
                                                          a.created_at,
                                                          u.given_name,
                                                          u.family_name,
                                                          u.email,
                                                          u.affiliation,
                                                          u.country,
                                                          a.reviewer_assigned,
                                                          a.review_start_date,
                                                          a.review_end_date,
                                                          a.copyeditor,
                                                          a.stage
                                                      FROM article a
                                                      JOIN user u ON a.user = u.email
                                                      WHERE a.status = 'Approved'
            
            """, nativeQuery = true)
    List<Object[]> getApprovedSql();

    @Query(value = """
                SELECT
                                                          a.id,
                                                          a.reviewer_comment,
                                                          a.section,
                                                          a.comment,
                                                          a.pdf,
                                                          a.status,
                                                          a.user,
                                                          a.approved_by,
                                                          a.prefix,
                                                          a.title,
                                                          a.subtitle,
                                                          a.abstracts,
                                                          a.keywords,
                                                          a.reference_text,
                                                          a.created_at,
                                                          u.given_name,
                                                          u.family_name,
                                                          u.email,
                                                          u.affiliation,
                                                          u.country,
                                                          a.reviewer_assigned,
                                                          a.review_start_date,
                                                          a.review_end_date,
                                                          a.copyeditor,
                                                          a.stage
                                                      FROM article a
                                                      JOIN user u ON a.user = u.email
                                                      WHERE a.status = 'Under Review'
            
            """, nativeQuery = true)
    List<Object[]> getUnderReview();

    @Query(value = """
                SELECT
                                                          a.id,
                                                          a.reviewer_comment,
                                                          a.section,
                                                          a.comment,
                                                          a.pdf,
                                                          a.status,
                                                          a.user,
                                                          a.approved_by,
                                                          a.prefix,
                                                          a.title,
                                                          a.subtitle,
                                                          a.abstracts,
                                                          a.keywords,
                                                          a.reference_text,
                                                          a.created_at,
                                                          u.given_name,
                                                          u.family_name,
                                                          u.email,
                                                          u.affiliation,
                                                          u.country,
                                                          a.reviewer_assigned,
                                                          a.review_start_date,
                                                          a.review_end_date,
                                                          a.copyeditor,
                                                          a.stage,
                                                          a.under_review_comments,
                                                          a.user_comments,
                                                          a.modify_copy_editor
                                                      FROM article a
                                                      JOIN user u ON a.user = u.email
                                                      WHERE a.status = 'Copy Editor'
            
            """, nativeQuery = true)
    List<Object[]> getCopyEditor();

    @Query(value = """
                SELECT
                                                          a.id,
                                                          a.reviewer_comment,
                                                          a.section,
                                                          a.comment,
                                                          a.pdf,
                                                          a.status,
                                                          a.user,
                                                          a.approved_by,
                                                          a.prefix,
                                                          a.title,
                                                          a.subtitle,
                                                          a.abstracts,
                                                          a.keywords,
                                                          a.reference_text,
                                                          a.created_at,
                                                          u.given_name,
                                                          u.family_name,
                                                          u.email,
                                                          u.affiliation,
                                                          u.country,
                                                          a.reviewer_assigned,
                                                          a.review_start_date,
                                                          a.review_end_date,
                                                          a.copyeditor,
                                                          a.stage,
                                                          a.user_production_comments,
                                                          a.production_comments,
                                                          a.modify_production,
                                                          a.final_file
                                                      FROM article a
                                                      JOIN user u ON a.user = u.email
                                                      WHERE a.status = 'Production'
            
            """, nativeQuery = true)
    List<Object[]> getProduction();


    @Query("SELECT a FROM Article  a WHERE a.status='approved' ")
    List<Article> getApprovedArticles();

    @Query("SELECT a FROM Article  a WHERE a.status='rejected' ")
    List<Article> getRejectedArticles();

//    List<Contributors> findByArticle

}
