package com.example.journals_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ ADD THIS LINE
                .csrf(csrf -> csrf.disable()) // disable CSRF for form uploads
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/**",
                                "/api/sign-up",
                                "/api/get-users",
                                "/api/verify",
                                "/api/template",
                                "/api/uploadFinal",

                                "/api/upload-form", // explicitly allow your upload route
                                "/uploads/**",
                                "/api/article_form",// allow serving uploaded files
                                "/api/get",// allow serving uploaded files
                                "/**",
                                "/uploads/**",
                                "/admin/login",
                                "/admin/register",
                                "/notification/add",
                                "/api/search",
                                // API endpoints
                                "/admin/login-page",
                                "/admin/approved",
                                "/admin/register-page" // Thymeleaf pages// allow other frontend routes if needed
                        ).permitAll()
                        .anyRequest().permitAll() // allow all others
                )
                .formLogin(form -> form.disable()) // disable default login form
                .httpBasic(basic -> basic.disable()); // disable basic auth

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",  // Your Next.js frontend
                "http://localhost:3001",
                "https://journals-git-main-wafa3067s-projects.vercel.app"
        ));
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));
        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}