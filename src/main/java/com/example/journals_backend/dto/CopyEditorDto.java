package com.example.journals_backend.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopyEditorDto {
    private Long id;
    private String reviewerComment;
    private String section;
    private String comment;
    private String pdf;
    private String status;
    private String user;
    private String approvedBy;
    private String prefix;
    private String title;
    private String subtitle;
    private String abstracts;
    private String keywords;
    private String referenceText;
    private String createdAt;

    private String givenName;
    private String familyName;
    private String email;
    private String affiliation;
    private String country;
    private String reviewerAssigned;   // Reviewer name
    private String reviewStartDate;    // Review start date
    private String reviewEndDate;      // Review end date
    private String copyeditor;         // Copyeditor name
    private String stage;

    @Lob
    private String underReviewComments;
    private String userComments;
    private  boolean modifyCopyEditor;
}
