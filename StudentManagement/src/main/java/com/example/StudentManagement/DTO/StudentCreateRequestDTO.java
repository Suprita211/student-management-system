package com.example.StudentManagement.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCreateRequestDTO {

    // PERSON_MASTER

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

    // STUDENT

    private String studentId;
    private String courseName;
    private String courseType;
    private String universityRegistrationNo;
    private LocalDate dateOfAdmission;
    private String counsellorName;
    private String session;
    private String duration;
}