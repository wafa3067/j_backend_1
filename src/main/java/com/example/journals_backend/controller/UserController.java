package com.example.journals_backend.controller;

import com.example.journals_backend.admin.JwtUtil;
import com.example.journals_backend.dto.LoginRequest;
import com.example.journals_backend.entity.User;
import com.example.journals_backend.repo.UserRepo;
import com.example.journals_backend.service.UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserSerivce userSerivce;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepo userRepo;

    // Helper: find user by email
    private User getUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return user;
    }


    @PostMapping("/update/identity")
    public String updateIdentity(@RequestParam String email, @RequestBody User updatedUser){
        User user = getUserByEmail(email);
        if (updatedUser.getGivenName() != null) user.setGivenName(updatedUser.getGivenName());
        if (updatedUser.getFamilyName() != null) user.setFamilyName(updatedUser.getFamilyName());
        if (updatedUser.getPublicName() != null) user.setPublicName(updatedUser.getPublicName());
        userRepo.save(user);
        return "User identity updated successfully!";
    }

    @PostMapping("/update/contact")
    public  String updateContact(@RequestParam String email, @RequestBody User updatedUser) {
        User user = getUserByEmail(email);
        if (updatedUser.getSignature() != null) user.setSignature(updatedUser.getSignature());
        if (updatedUser.getPhone() != null) user.setPhone(updatedUser.getPhone());
        if (updatedUser.getMaillingAddress() != null) user.setMaillingAddress(updatedUser.getMaillingAddress());
        if (updatedUser.getAffiliation() != null) user.setAffiliation(updatedUser.getAffiliation());
        if (updatedUser.getCountry() != null) user.setCountry(updatedUser.getCountry());
        // Languages (if any)
        if (updatedUser.getLanguagesList() != null && !updatedUser.getLanguagesList().isEmpty()) {
            user.setLanguagesList(updatedUser.getLanguagesList());
        }
        userRepo.save(user);
        return "User Contact updated successfully!";

    }

    @PostMapping("/update/role")
    public  String updateRole(@RequestParam String email, @RequestBody User updatedUser) {
        User user = getUserByEmail(email);


        if (updatedUser.getReview() != null) user.setReview(updatedUser.getReview());
        if (updatedUser.getRole() != null && !updatedUser.getRole().isEmpty()) {
            user.setRole(updatedUser.getRole());
        }
        userRepo.save(user);
        return "User role updated successfully!";
    }
    // ✅ PATCH: update all fields except username & email
    @PostMapping("/update/public")
    public String updateUser(
            @RequestParam String email,
            @RequestPart(required = false) MultipartFile profile,
            @RequestPart("user") User updatedUser
    ) {
        User user = getUserByEmail(email);

        try {
            // ✅ Store inside resources/static/uploads/profile_images
            String uploadDir = new File("src/main/resources/static/uploads/profile_images").getAbsolutePath();
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            if (profile != null && !profile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + profile.getOriginalFilename();
                File dest = new File(dir, fileName);
                profile.transferTo(dest);

                // ✅ Save relative path (accessible via HTTP)
                user.setProfile("/uploads/profile_images/" + fileName);
            }

            if (updatedUser.getBio() != null) user.setBio(updatedUser.getBio());
            if (updatedUser.getHomeurl() != null) user.setHomeurl(updatedUser.getHomeurl());
            if (updatedUser.getOrcid_id() != null) user.setOrcid_id(updatedUser.getOrcid_id());

            userRepo.save(user);
            return "User profile updated successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error updating user: " + e.getMessage();
        }
    }




    @PatchMapping("/update/password")
    public ResponseEntity<String> updatePassword(
            @RequestParam String email,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found!");
        }

        // ✅ Check if old password matches stored password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Incorrect old password!");
        }

        // ✅ Encode and update new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        return ResponseEntity.ok("Password updated successfully!");
    }

    @GetMapping("/find")
    public ResponseEntity<?> getUserWithEmail(@RequestParam String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return ResponseEntity.ok(Map.of("email", "Email not found"+email));
        }
        return ResponseEntity.ok(Map.of("message", "Password updated successfully",
                "user", user));
    }



    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> userRegister(@RequestBody User user) {
        String result = userSerivce.registerUser(user);

        if ("Email already registered!".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", result));
        }

        return ResponseEntity.ok(Map.of("message", result));
    }



    @GetMapping("/get-users")
    public List<User> getUser(){
        return  userSerivce.getAllUsers();

    }
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token){
        boolean verified=userSerivce.verfyUser(token);
        return verified ? "Email verified successfully!" : "Invalid or expired token!";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
        return userSerivce.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        boolean valid = jwtUtil.validateToken(token);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String oldToken) {
        if (!jwtUtil.validateToken(oldToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired or invalid");
        }
        String username = jwtUtil.extractUsername(oldToken);
        String newToken = jwtUtil.generateToken(username);
        return ResponseEntity.ok(Map.of("token", newToken));
    }


    

}
