package com.example.StudentManagement.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDTO {

    private String studentId;

    private Long personId;

    private String aadhaarNo;

    private String fullName;

    private String fatherName;

    private String motherName;

    private LocalDate dateOfBirth;

    private String primaryContact;

    private String secondaryContact;

    private String email;

    private String presentAddress;

    private String permanentAddress;

    private String courseName;

    private String courseType;

    private String universityRegistrationNo;

    private LocalDate dateOfAdmission;

    private String counsellorName;

    private String session;

    private String duration;

    // NEW
    private List<DocumentResponseDTO> documents;
}