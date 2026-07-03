package com.example.StudentManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentSearchResponseDTO {

    private String studentId;

    private String fullName;

    private String aadhaarNo;

    private String fatherName;
    private String universityName;

    private String motherName;

    private String primaryContact;

    private String secondaryContact;

    private String email;

    private String presentAddress;

    private String permanentAddress;

    private LocalDate dateOfBirth;

    private String courseName;

    private String courseType;

    private String universityRegistrationNo;

    private LocalDate dateOfAdmission;

    private String counsellorName;

    private String session;

    private String duration;

    private List<DocumentResponseDTO> documents;
}