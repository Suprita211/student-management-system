package com.example.StudentManagement.Service.Impl;


import com.example.StudentManagement.DTO.DashboardResponseDTO;
import com.example.StudentManagement.Repository.StudentDocumentRepository;
import com.example.StudentManagement.Repository.StudentRepository;
import com.example.StudentManagement.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl
        implements AdminService {

    private final StudentRepository studentRepository;

    private final StudentDocumentRepository
            studentDocumentRepository;

    @Override
    public DashboardResponseDTO getDashboard() {

        return DashboardResponseDTO.builder()
                .totalStudents(
                        studentRepository.count()
                )
                .totalDocuments(
                        studentDocumentRepository.count()
                )
                .totalCourses(
                        studentRepository
                                .countDistinctCourses()
                )
                .build();
    }
}