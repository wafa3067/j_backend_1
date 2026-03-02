//package com.example.journals_backend.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations("file:uploads/");
//    }
//}
package com.example.journals_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow requests from frontend (localhost:3000)
        registry.addMapping("/**").allowedOrigins("https://journals-git-main-wafa3067s-projects.vercel.app")
        ;
    }
//for local
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // Use the absolute path for your local uploads directory
//        String uploadDir = "/Users/wafaabbas/Documents/spring/journals_backend/uploads/"; // Correct path
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations("file:" + uploadDir);  // Use the correct absolute file path
//    }
//    for ec2 aws
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // Use System.getProperty("user.dir") to match the upload logic
    String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:" + uploadDir);
}

}
