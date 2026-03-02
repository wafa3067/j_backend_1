package com.example.journals_backend.service;

import com.example.journals_backend.admin.JwtUtil;
import com.example.journals_backend.entity.User;
import com.example.journals_backend.entity.VerificationToken;
import com.example.journals_backend.repo.UserRepo;
import com.example.journals_backend.repo.VerificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Service
public class UserSerivce {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private VerificationRepo verificationRepo;
    @Autowired
    private EmailService emailService;


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(User user){
        User userOpt = userRepo.findByEmail(user.getEmail());

        if (userOpt!=null) {
            return "Email already registered!";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailVerified(false);
        userRepo.save(user);

//        VerificationToken verificationToken=new VerificationToken(user);
//        verificationRepo.save(verificationToken);

//        emailService.sendEmailVerification(user.getEmail(), verificationToken.getToken());
        return "Registration successfully";

    }

    public String sendMail( String toEmail,
                            String authorName,
                            String articleTitle,
                            String status,
                            String body
                          ){
        emailService.sendArticleUnderReviewEmailHtml(toEmail,authorName,articleTitle,status,body);
        return "Email Send";
    }


    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, String> login(String email, String password) {
        Optional<User> optionalUser = Optional.ofNullable(userRepo.findByEmail(email));

        if (optionalUser.isEmpty()) {
            return Map.of(
                    "error","Invalid email or password",
                    "id","",
                    "email",""
                    ); // can keep plain string
        }

        User user = optionalUser.get();

//        if (!user.isEmailVerified()) {
//            return Map.of(
//                    "error","Please verify your email address first",
//                    "id","",
//                    "email",""
//            );
//        }

        if (!passwordEncoder.matches(password, user.getPassword())) {

            return  Map.of(
                    "error","Invalid email or password",
                    "id","",
                    "email",""

            );
        }

        // Generate JWT token for frontend
        String token = jwtUtil.generateToken(user.getUsername()); // or email
       var data=List.of(token,user.getId().toString());
        return Map.of(
                "error","",
                "id",user.getId().toString(),
                "email",user.getEmail(),
                "name",user.getGivenName(),

                "token",token

        );
    }



    public  boolean verfyUser(String token){

        VerificationToken token1=verificationRepo.findByToken(token);
        if (token1 == null) return false;
        User user=token1.getUser();
        user.setEmailVerified(true);
        userRepo.save(user);
        verificationRepo.delete(token1);
        return true;

    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }


    public String checkTokenStatus(String token) {
        if (!jwtUtil.validateToken(token)) {
            return "Token is invalid or expired";
        }
        String username = jwtUtil.extractUsername(token);
        return "Token is valid for user: " + username;
    }

    public void updateStatus(Long id, String status) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        userRepo.save(user);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }




}
