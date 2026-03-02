package com.example.journals_backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String givenName;
    private String joinedDate;
    private String publicName;
    private String signature;
    private String phone;
    private String maillingAddress;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Language> role;
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'active'")
    private String status;
    private String familyName;
    private String affiliation;
    private String country;
    @Column(unique = true)
    private String email;
    private String profile;
    @Lob
    private String bio;
    private String homeurl;
    private String orcid_id;

    private String username;
    private String password;
    private boolean privacyAgree=false;
    private boolean enableNotification=false;
    private String review;
    private boolean emailVerified=false;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Language> languagesList;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private VerificationToken verificationToken;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private VerificationToken verificationToken;



}
