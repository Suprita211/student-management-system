package com.example.StudentManagement.Service;

import com.example.StudentManagement.DTO.StudentCreateRequestDTO;
import com.example.StudentManagement.DTO.StudentResponseDTO;
import com.example.StudentManagement.DTO.StudentUpdateRequestDTO;
import com.example.StudentManagement.Entity.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {
    StudentResponseDTO createStudentAdmission(
            StudentCreateRequestDTO request
    );

    StudentResponseDTO updateStudentAdmission(
            String studentId,
            StudentUpdateRequestDTO request
    );

    StudentResponseDTO getStudentById(
            String studentId
    );

    Page<StudentResponseDTO> getAllStudents(
            int page,
            int size
    );
    byte[] generateStudentPdf(String studentId);
}
