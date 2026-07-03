package com.example.StudentManagement.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCreateRequestDTO {

    // PERSON_MASTER

    @NotBlank(message = "Aadhaar Number is required")
    @Pattern(
            regexp = "^[0-9]{12}$",
            message = "Aadhaar must be 12 digits"
    )
    private String aadhaarNo;

    @NotBlank(message = "Full Name is required")
    private String fullName;

    private String fatherName;

    private String motherName;

    @NotNull(message = "Date Of Birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Primary Contact is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must be 10 digits"
    )
    private String primaryContact;
    private String universityName;

    private String secondaryContact;

    @Email(message = "Invalid email format")
    private String email;

    private String presentAddress;

    private String permanentAddress;

    // STUDENT

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Course Name is required")
    private String courseName;

    @NotBlank(message = "Course Type is required")
    private String courseType;

    private String universityRegistrationNo;

    @NotNull(message = "Date Of Admission is required")
    private LocalDate dateOfAdmission;

    private String counsellorName;

    @NotBlank(message = "Session is required")
    private String session;

    private String duration;
}