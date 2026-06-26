package com.example.StudentManagement.Config;

import com.example.StudentManagement.Service.JwtService;
import com.example.StudentManagement.Service.Impl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("=================================");
        System.out.println("REQUEST URI = " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");

        System.out.println("AUTH HEADER = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            System.out.println("NO BEARER TOKEN FOUND");

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        System.out.println("TOKEN = " + token);

        String username;

        try {

            username = jwtService.extractUsername(token);

            System.out.println("USERNAME FROM TOKEN = " + username);

        } catch (Exception e) {

            System.out.println("JWT PARSE ERROR = " + e.getMessage());

            filterChain.doFilter(request, response);
            return;
        }

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            try {

                UserDetails userDetails =
                        customUserDetailsService.loadUserByUsername(username);

                System.out.println("USER FOUND = " + userDetails.getUsername());

                System.out.println(
                        "AUTHORITIES = " +
                                userDetails.getAuthorities()
                );

                boolean valid =
                        jwtService.isTokenValid(token, userDetails);

                System.out.println("TOKEN VALID = " + valid);

                if (valid) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);

                    System.out.println("AUTHENTICATION SET SUCCESSFULLY");
                }

            } catch (Exception e) {

                System.out.println("USER LOAD ERROR = " + e.getMessage());
            }
        }

        System.out.println("REQUEST URI = " + request.getRequestURI());
        System.out.println("AUTHENTICATION = " +
                SecurityContextHolder.getContext().getAuthentication());
        filterChain.doFilter(request, response);
    }
}