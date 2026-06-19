package com.example.StudentManagement.Service;

import com.example.StudentManagement.Entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateToken(User user);

    public String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
}
