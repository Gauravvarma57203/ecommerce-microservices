package com.ecommerce.auth.exception;

import jakarta.servlet.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String json = """
            {
                "success": false,
                "message": "Unauthorized access - invalid or missing token",
                "status": 401,
                "path": "%s",
                "timestamp": "%s"
            }
            """.formatted(request.getRequestURI(), Instant.now().toString());

        response.getWriter().write(json);
    }
}
