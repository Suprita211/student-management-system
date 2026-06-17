package com.example.StudentManagement.Service.Impl;

import com.example.StudentManagement.DTO.AuthResponseDTO;
import com.example.StudentManagement.DTO.LoginRequestDTO;
import com.example.StudentManagement.DTO.SignupRequestDTO;
import com.example.StudentManagement.Entity.User;
import com.example.StudentManagement.Repository.StudentRepository;
import com.example.StudentManagement.Repository.UserRepository;
import com.example.StudentManagement.Service.AuthService;
import com.example.StudentManagement.Service.EmailService;
import com.example.StudentManagement.Service.JwtService;
import com.example.StudentManagement.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public String signup(SignupRequestDTO request) {

        if(userRepository.existsByUsername(
                request.getUsername())) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);

        return "Signup successful";
    }

    @Override
    public AuthResponseDTO login(
            LoginRequestDTO request
    ) {

        User user = userRepository
                .findByUsername(
                        request.getUsername()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid Username or Password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException(
                    "Invalid Email or Password"
            );
        }

        String token =
                jwtService.generateToken(user);

        return AuthResponseDTO.builder()
                .token(token)
                .message("Login successful")
                .role(user.getRole().name())
                .build();
    }
    @Override
    public String forgotPassword( String username) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        String otp = String.valueOf(
                100000 + new Random().nextInt(900000)
        );

        user.setOtp(otp);

        user.setOtpExpiryTime(
                LocalDateTime.now().plusMinutes(5)
        );

        user.setOtpVerified(false);

        userRepository.save(user);

        // TODO: Send email here

        emailService.sendOtp(
                "suman@nimttgroup.com",
                otp
        );

        return "OTP sent successfully";
    }
    @Override
    public String resetPassword(
            String username,
            String newPassword
    ) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        if (!user.isOtpVerified()) {

            throw new RuntimeException(
                    "Verify OTP first"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        newPassword
                )
        );

        user.setOtp(null);

        user.setOtpExpiryTime(null);

        user.setOtpVerified(false);

        userRepository.save(user);

        return "Password reset successful";
    }
    @Override
    public String verifyOtp(
            String username,
            String otp
    ) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        if (user.getOtp() == null) {

            throw new RuntimeException(
                    "OTP not generated"
            );
        }

        if (!user.getOtp().equals(otp)) {

            throw new RuntimeException(
                    "Invalid OTP"
            );
        }

        if (LocalDateTime.now().isAfter(
                user.getOtpExpiryTime()
        )) {

            throw new RuntimeException(
                    "OTP expired"
            );
        }

        user.setOtpVerified(true);

        userRepository.save(user);

        return "OTP verified successfully";
    }
}


