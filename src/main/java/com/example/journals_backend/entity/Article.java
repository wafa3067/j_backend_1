package com.example.journals_backend.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewerComment;
    @Lob
    private String underReviewComments;
    @Lob
    private String userComments;
    @Lob
    private String userProductionComments;
    @Lob
    private String productionComments;

    private boolean modifyCopyEditor;
    private boolean modifyProduction;
    private String section;
    private String previousPublished;
    private String fileType;
    private String refrencedUrl;
    private String textStyle;
    private String textStylic;
    @Lob
    private String comment;
    private String copyright;
    private String privacypolicy;


    @Lob
    @Column(name = "pdf_data", columnDefinition = "LONGBLOB") // MySQL
    private byte[] pdfData;
    @Lob

    @Column(name = "pdf_url") // Store the file URL in the database
    private String pdfUrl;


    @Column(name = "final_file") // Store the file URL in the database
    private String finalFile;

    private String pdfFileName;   // Store the original file name for reference
    private String pdfContentType; // Store the MIME type (e.g., application/pdf)

    private String pdf;
    private String status;
    private String user;
    private String approvedBy;

    private String prefix;
    private String title;
    private String subtitle;
   @Lob
    private String abstracts;
    @Lob
    private String keywords;
    @Lob
    private String referenceText;
    private String createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Contributors> contributorsList;

    // --- New fields for review and production workflow ---
    private String reviewerAssigned; // e.g., Prof. Sara Malik
    private String submittedOn;      // e.g., 2025-10-24
    private String reviewStarted;    // e.g., 2025-10-28
    private String reviewDeadline;   // e.g., 2025-11-10

//    private String copyeditor;       // e.g., Dr. Ayesha Noor
    private String doi;              // e.g., 10.1234/jjp.2025.001
    private String volumeIssue;      // e.g., Vol. 18 - Issue 4
    @Lob
    private String productionNotes;  // e.g., Proof sent to author...
    @Lob
    private String copyeditorNotes;  // e.g., Submitted: 2025-10-14, Status: In Progress
    private String reviewStartDate;    // Review start date
    private String reviewEndDate;      // Review end date
    private String copyeditor;         // Copyeditor name
    private String stage;




}


