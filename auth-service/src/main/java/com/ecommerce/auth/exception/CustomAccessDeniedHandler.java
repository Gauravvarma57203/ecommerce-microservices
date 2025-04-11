package com.ecommerce.auth.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        String json = """
            {
                "success": false,
                "message": "Access denied. Admins only.",
                "status": 403,
                "path": "%s",
                "timestamp": "%s"
            }
            """.formatted(request.getRequestURI(), Instant.now());

        response.getWriter().write(json);
    }
}
