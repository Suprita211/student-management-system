package com.example.StudentManagement.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestDTO {

    private String username;

    private String newPassword;
}