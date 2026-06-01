package com.example.StudentManagement.Service.Impl;


import com.example.StudentManagement.Entity.User;
import com.example.StudentManagement.Service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY =
            "mysecretkeymysecretkeymysecretkey123456";

    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(
                        StandardCharsets.UTF_8
                )
        );
    }

    @Override
    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim(
                        "role",
                        user.getRole().name()
                )
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 86400000
                        )
                )
                .signWith(
                        getSigningKey()
                )
                .compact();
    }

    @Override
    public String extractEmail(String token) {

        return extractClaims(token)
                .getSubject();
    }

    @Override
    public boolean isTokenValid(String token) {

        return extractClaims(token)
                .getExpiration()
                .after(new Date());
    }

    private Claims extractClaims(
            String token
    ) {

        return Jwts.parserBuilder()
                .setSigningKey(
                        getSigningKey()
                )
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}