package com.example.StudentManagement.DTO;



import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {

    private Long totalStudents;

    private Long totalDocuments;

    private Long totalCourses;
}