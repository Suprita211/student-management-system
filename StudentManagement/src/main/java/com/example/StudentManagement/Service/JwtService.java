package com.example.StudentManagement.Service;

import com.example.StudentManagement.Entity.User;

public interface JwtService {

    String generateToken(User user);

    String extractEmail(String token);

    boolean isTokenValid(String token);
}
