package com.example.StudentManagement.DTO;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequestDTO {



    @Email
    private String email;

    @NotBlank
    private String password;



}