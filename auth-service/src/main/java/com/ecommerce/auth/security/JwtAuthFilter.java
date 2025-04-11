package com.ecommerce.auth.security;


import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            final String email = jwtUtil.extractEmail(token);

            if (email != null && jwtUtil.isTokenValid(token)) {
                var userDetails = userDetailsService.loadUserByUsername(email);

                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            sendErrorResponse(response, "Token has expired", request.getRequestURI(), 401);
        } catch (UnsupportedJwtException ex) {
            sendErrorResponse(response, "Unsupported JWT token", request.getRequestURI(), 401);
        } catch (MalformedJwtException ex) {
            sendErrorResponse(response, "Malformed JWT token", request.getRequestURI(), 401);
        } catch (SignatureException ex) {
            sendErrorResponse(response, "Invalid JWT signature", request.getRequestURI(), 401);
        } catch (IllegalArgumentException ex) {
            sendErrorResponse(response, "Token claims are empty", request.getRequestURI(), 401);
        } catch (Exception ex) {
            sendErrorResponse(response, "Invalid or tampered token", request.getRequestURI(), 401);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message, String path, int statusCode) throws IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);
        String json = """
                {
                    "success": false,
                    "message": "%s",
                    "status": %d,
                    "path": "%s",
                    "timestamp": "%s"
                }
                """.formatted(message, statusCode, path, Instant.now());
        response.getWriter().write(json);
    }
}
