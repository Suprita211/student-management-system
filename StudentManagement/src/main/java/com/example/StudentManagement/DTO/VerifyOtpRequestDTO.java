package com.example.StudentManagement.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequestDTO {

    private String username;

    private String otp;
}