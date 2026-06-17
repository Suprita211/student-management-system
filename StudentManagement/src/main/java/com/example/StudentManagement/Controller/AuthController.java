package com.example.StudentManagement.Controller;

import com.example.StudentManagement.DTO.*;
import com.example.StudentManagement.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
   

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @Valid
            @RequestBody
            SignupRequestDTO request
    ) {

        return ResponseEntity.ok(
                authService.signup(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid
            @RequestBody LoginRequestDTO request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequestDTO request
    ) {

        System.out.println("Forgot Password API Hit");

        return ResponseEntity.ok(
                authService.forgotPassword(
                        request.getUsername()
                )
        );
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequestDTO request
    ) {
        return ResponseEntity.ok(
                authService.resetPassword(
                        request.getUsername(),
                        request.getNewPassword()
                )
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @RequestBody VerifyOtpRequestDTO request
    ) {

        return ResponseEntity.ok(
                authService.verifyOtp(
                        request.getUsername(),
                        request.getOtp()
                )
        );
    }
}