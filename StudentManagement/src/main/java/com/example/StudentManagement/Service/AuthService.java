package com.example.StudentManagement.Service;


import com.example.StudentManagement.DTO.AuthResponseDTO;
import com.example.StudentManagement.DTO.LoginRequestDTO;
import com.example.StudentManagement.DTO.SignupRequestDTO;

public interface AuthService {

    String signup(
            SignupRequestDTO request
    );

    AuthResponseDTO login(
            LoginRequestDTO request
    );
    public String forgotPassword(
            String username
    );

    String resetPassword(
            String username,
            String newPassword
    );
    String verifyOtp(
            String username,
            String otp
    );
}