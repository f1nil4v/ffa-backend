package com.ffa.tournaments.shared.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            AuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.jwtService = jwtService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!header.startsWith("Bearer ") || header.length() == 7) {
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new BadCredentialsException("Invalid authorization header")
            );
            return;
        }

        String token = header.substring(7);

        try {
            Claims claims = jwtService.extractClaims(token);
            String subject = claims.getSubject();

            if (subject == null) {
                throw new BadCredentialsException("Missing JWT subject");
            }

            UUID subjectId = UUID.fromString(subject);
            String role = claims.get("role", String.class);

            if (role == null) {
                role = JwtService.ROLE_PLAYER;
            }

            List<GrantedAuthority> authorities = resolveAuthorities(role);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                var authentication = new UsernamePasswordAuthenticationToken(
                        subjectId,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (JwtException | IllegalArgumentException | AuthenticationException exception) {
            logger.warn("JWT validation failed");

            SecurityContextHolder.clearContext();

            authenticationEntryPoint.commence(
                    request,
                    response,
                    new BadCredentialsException("Invalid JWT", exception)
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private List<GrantedAuthority> resolveAuthorities(String role) {
        return switch (role) {
            case JwtService.ROLE_PLAYER -> List.of(
                    new SimpleGrantedAuthority("ROLE_" + JwtService.ROLE_PLAYER)
            );
            case JwtService.ROLE_SUPPORT -> List.of(
                    new SimpleGrantedAuthority("ROLE_" + JwtService.ROLE_SUPPORT)
            );
            default -> throw new BadCredentialsException("Unknown JWT role");
        };
    }
}
