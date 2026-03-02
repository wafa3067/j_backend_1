package com.example.journals_backend.admin.adminservice;

import com.example.journals_backend.admin.AdminLogin;
import com.example.journals_backend.admin.adminrepo.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminLoginService {

    @Autowired
    private AdminRepository adminRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

    public AdminLogin registerAdmin(String email,String password){
        String encrypt=bCryptPasswordEncoder.encode(password);
        return adminRepository.save(new AdminLogin(email,encrypt));
    }

    public boolean validateAdmin(String email ,String password){
        Optional<AdminLogin> adminOpt= adminRepository.findByEmail(email);
        return adminOpt.isPresent() && bCryptPasswordEncoder.matches(password,adminOpt.get().getPassword());
    }



}
