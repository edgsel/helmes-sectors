package com.helmes.sectorsapi.config;

import com.helmes.sectorsapi.exception.BadCredentialsException;
import com.helmes.sectorsapi.exception.EntityNotFoundException;
import com.helmes.sectorsapi.repository.UserRepository;
import com.helmes.sectorsapi.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.helmes.sectorsapi.exception.ErrorCode.INVALID_CREDENTIALS;
import static com.helmes.sectorsapi.exception.ErrorCode.USER_NOT_FOUND;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.substring(7);
            String username;

            try {
                username = jwtService.getUsername(token);
            } catch (Exception _) {
                throw new BadCredentialsException("Invalid JWT token", INVALID_CREDENTIALS.name());
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("Username %s not found".formatted(username), USER_NOT_FOUND.name()));
                var auth = new UsernamePasswordAuthenticationToken(user, null, List.of());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }
        filterChain.doFilter(request, response);
    }
}
