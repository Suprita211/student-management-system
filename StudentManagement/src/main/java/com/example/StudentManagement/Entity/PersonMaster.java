package com.example.StudentManagement.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "person_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    @Column(name = "aadhaar_no", unique = true, nullable = false)
    private String aadhaarNo;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "primary_contact")
    private String primaryContact;

    @Column(name = "secondary_contact")
    private String secondaryContact;

    private String email;

    @Column(name = "present_address", columnDefinition = "TEXT")
    private String presentAddress;

    @Column(name = "permanent_address", columnDefinition = "TEXT")
    private String permanentAddress;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // One Person -> Many Student Admissions
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Student> students;
}
