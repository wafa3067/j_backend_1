package com.example.journals_backend.admin.adminrepo;

import com.example.journals_backend.admin.AdminLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminLogin,Long> {
    Optional<AdminLogin> findByEmail(String email);
}
