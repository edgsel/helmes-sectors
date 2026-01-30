package com.helmes.sectorsapi.config;

import com.helmes.sectorsapi.repository.UserRepository;
import com.helmes.sectorsapi.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final List<GrantedAuthority> authorities = List.of(); // add more user roles if needed

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isValidBearerAuthHeader(authHeader)) {
            try {
                setAuthentication(request, getUsernameFromJWT(authHeader));
            } catch (Exception ex) {
                log.warn("JWT authentication failed: {}", ex.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getUsernameFromJWT(String authHeader) {
        var token = authHeader.substring(7);
        return jwtService.getUsername(token);
    }

    private void setAuthentication(HttpServletRequest request, String username) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            var user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found in token"));
            var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    private static boolean isValidBearerAuthHeader(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }
}
