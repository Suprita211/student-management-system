package com.example.StudentManagement.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentUpdateRequestDTO {

    private String fullName;

    private String fatherName;

    private String motherName;

    private LocalDate dateOfBirth;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must be 10 digits"
    )
    private String primaryContact;

    private String secondaryContact;

    @Email(message = "Invalid email format")
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
}