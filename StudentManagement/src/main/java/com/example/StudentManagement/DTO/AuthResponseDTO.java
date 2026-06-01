package com.example.StudentManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;

    private String message;
    private String role;
}