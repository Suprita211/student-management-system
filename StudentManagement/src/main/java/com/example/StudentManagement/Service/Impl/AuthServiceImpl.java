package com.example.StudentManagement.Service.Impl;

import com.example.StudentManagement.DTO.AuthResponseDTO;
import com.example.StudentManagement.DTO.LoginRequestDTO;
import com.example.StudentManagement.DTO.SignupRequestDTO;
import com.example.StudentManagement.Entity.User;
import com.example.StudentManagement.Repository.StudentRepository;
import com.example.StudentManagement.Repository.UserRepository;
import com.example.StudentManagement.Service.AuthService;
import com.example.StudentManagement.Service.JwtService;
import com.example.StudentManagement.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.Builder;
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public String signup(SignupRequestDTO request) {

        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User user = User.builder()
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

        User user = userRepository.findByEmail(
                request.getEmail()
        ).orElseThrow(() ->
                new RuntimeException(
                        "Invalid Email or Password"
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
}


