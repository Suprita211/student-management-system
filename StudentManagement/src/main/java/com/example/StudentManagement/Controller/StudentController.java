package com.example.StudentManagement.Controller;

import com.example.StudentManagement.DTO.StudentCreateRequestDTO;
import com.example.StudentManagement.DTO.StudentResponseDTO;
import com.example.StudentManagement.DTO.StudentUpdateRequestDTO;
import com.example.StudentManagement.Entity.Student;
import com.example.StudentManagement.Service.StudentService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // CREATE

    @PostMapping
    public StudentResponseDTO createAdmission(
            @RequestBody StudentCreateRequestDTO request
    ) {

        return studentService.createStudentAdmission(request);
    }

    // UPDATE

    @PutMapping("/{studentId}")
    public StudentResponseDTO updateStudent(
            @PathVariable String studentId,
            @RequestBody StudentUpdateRequestDTO request
    ) {

        return studentService.updateStudentAdmission(
                studentId,
                request
        );
    }

    // GET SINGLE

    @GetMapping("/{studentId}")
    public StudentResponseDTO getStudentById(
            @PathVariable String studentId
    ) {

        return studentService.getStudentById(studentId);
    }

    // GET ALL WITH PAGINATION

    @GetMapping
    public Page<StudentResponseDTO> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return studentService.getAllStudents(page, size);
    }
}