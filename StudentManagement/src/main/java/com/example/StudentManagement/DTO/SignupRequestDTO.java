package com.example.StudentManagement.DTO;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequestDTO {

    private String username;

    @Email
    private String email;

    private String password;
}