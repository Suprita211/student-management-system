package com.example.StudentManagement.Entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @Column(name = "student_id")
    private String studentId; // Accounts System Student ID (ST101)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private PersonMaster person;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course_type")
    private String courseType;

    @Column(name = "university_registration_no")
    private String universityRegistrationNo;

    @Column(name = "university_name")
    private String universityName;

    @Column(name = "date_of_admission")
    private LocalDate dateOfAdmission;

    @Column(name = "counsellor_name")
    private String counsellorName;

    private String session;

    private String duration;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // One Student -> Many Documents
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentDocument> documents = new ArrayList<>();

}