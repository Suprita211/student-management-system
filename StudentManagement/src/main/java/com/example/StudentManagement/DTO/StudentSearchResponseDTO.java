package com.example.StudentManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentSearchResponseDTO {

    private String studentId;

    private String fullName;

    private String aadhaarNo;

    private String courseName;

    private String courseType;

    private String session;

    private String duration;

    private List<DocumentResponseDTO> documents;
}