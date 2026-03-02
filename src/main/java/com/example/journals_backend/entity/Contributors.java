package com.example.journals_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Contributors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String giveName;
    private String publicName;
    private String homeurl;
    private String orcid;
    private String bio;
    private String email;
    private String role;
    private String country;
    private String  affiliation;
    private boolean primaryContact;
    private boolean principleContact;
    private boolean inBrowserlist;


}
