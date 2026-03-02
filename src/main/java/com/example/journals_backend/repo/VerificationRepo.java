package com.example.journals_backend.repo;

import com.example.journals_backend.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepo extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);
}
