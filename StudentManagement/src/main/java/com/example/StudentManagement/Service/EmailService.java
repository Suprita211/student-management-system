package com.example.StudentManagement.Service;
public interface EmailService {

    void sendOtp(
            String toEmail,
            String otp
    );
}