package com.example.journals_backend.admin.admincontroller;


import com.example.journals_backend.admin.JwtUtil;
import com.example.journals_backend.admin.adminservice.AdminLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/admin/login")
public class AdminLoginController {

    @Autowired
    private AdminLoginService adminService;

    @Autowired
    private com.example.journals_backend.admin.JwtUtil jwtUtil;

    @PostMapping("/register")
    public Map<String, String> register(@RequestParam String username, @RequestParam String password) {
        adminService.registerAdmin(username, password);
        return Map.of("message", "Admin registered successfully ✅");
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
        boolean valid = adminService.validateAdmin(username, password);
        if (!valid) {
            return Map.of("error", "Invalid username or password ❌");
        }
        String token = jwtUtil.generateToken(username);
        return Map.of("message", "Login successful ✅", "token", token);
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "Welcome to Admin Dashboard 👑";
    }
}

