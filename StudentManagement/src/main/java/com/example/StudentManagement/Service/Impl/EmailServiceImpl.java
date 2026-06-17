package com.example.StudentManagement.Service.Impl;

import com.example.StudentManagement.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtp(
            String toEmail,
            String otp
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject(
                "Password Reset OTP"
        );

        message.setText(
                "Your OTP is: "
                        + otp +
                        "\nValid for 5 minutes."
        );

        mailSender.send(message);
    }
}