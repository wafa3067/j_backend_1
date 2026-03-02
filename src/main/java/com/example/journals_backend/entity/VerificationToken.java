package com.example.journals_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VerificationToken {
    private static final int expiry=60*24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;


    private Date expiryDate;

    public VerificationToken(User user){
        this.user=user;
        this.token= UUID.randomUUID().toString();
        this.expiryDate=new Date(System.currentTimeMillis()+expiry*60*1000);
    }

}
