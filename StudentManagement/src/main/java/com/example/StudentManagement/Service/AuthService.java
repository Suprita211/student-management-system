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
}