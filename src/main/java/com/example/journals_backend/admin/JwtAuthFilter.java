package com.example.journals_backend.admin;


import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtAuthFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        // Allow the '/uploads/**' route without a token


        // ✅ Allow public endpoints (no token needed)
        if (uri.startsWith("/admin/login/login") ||
                uri.startsWith("/admin/login/register") ||
                uri.startsWith("/admin/register-page") ||
                uri.startsWith("/admin/login-page") ||
                uri.startsWith("/api/sign-up") ||
                uri.startsWith("/api/get-users") ||
                uri.startsWith("/api/login") ||
                uri.startsWith("/api/get") ||
                uri.startsWith("/api/users") ||
                uri.startsWith("/api/template") ||
                uri.startsWith("/api/uploadFinal") ||

                uri.startsWith("/api/refresh-token") ||
                uri.startsWith("/api/updatePdfAndComments") ||
                uri.startsWith("/api/updateComments") ||
                uri.startsWith("/api/updateProduction") ||
                uri.startsWith("/api/modify_production") ||
                uri.startsWith("/api/modify_copy") ||
                uri.startsWith("/api/updateUserProduction") ||
                uri.startsWith("/api/pending-articles") ||
                uri.startsWith("/uploads/") ||
                uri.startsWith("/api/rejected-articles") ||
                uri.startsWith("/api/approved-articles") ||
                uri.startsWith("/api/verify") ||
                uri.startsWith("/api/search") ||
                uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images")
       ||  uri.startsWith("/admin/dashboard") ||
                uri.startsWith("/admin/articles") ||
                uri.startsWith("/admin/approve") ||
                uri.startsWith("/admin/articles/reject")||
                uri.startsWith("/admin/pendding")||
                uri.startsWith("/admin/email")||
                uri.startsWith("/admin/pending")||
                uri.startsWith("/admin/copy-editor")||
                uri.startsWith("/admin/reject")||
                uri.startsWith("/admin/rejected")||
                uri.startsWith("/admin/approved")||
                uri.startsWith("/admin/users")||
                uri.startsWith("/admin/articles/modify-production/")||
                uri.startsWith("/admin/articles/modify-copy/")||
                uri.startsWith("/admin/users/status")||
                uri.startsWith("/api/validate-token")||
                uri.startsWith("/notification/get_notifications")||
                uri.startsWith("/notification/add")||
                uri.startsWith("/notification/get_all")||
                uri.startsWith("/notification/set")||
                uri.startsWith("/notification/count_unread")||
                uri.startsWith("/notification/mark_all_read")||
                uri.startsWith("/api/article/")||
                uri.startsWith("/admin/get-production")||
                uri.startsWith("/admin/add")||
                uri.startsWith("/admin/articles/status/")||
                uri.startsWith("/admin/setting")||
                uri.startsWith("/admin")
        ) {
            chain.doFilter(request, response);
            return;
        }

        // ✅ Token validation for protected routes
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                return;
            }
        } else {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token required");
            return;
        }

        chain.doFilter(request, response);
    }

}
