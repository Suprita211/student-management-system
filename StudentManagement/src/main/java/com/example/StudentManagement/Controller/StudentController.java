package com.example.StudentManagement.Controller;

import com.example.StudentManagement.DTO.StudentCreateRequestDTO;
import com.example.StudentManagement.DTO.StudentResponseDTO;
import com.example.StudentManagement.DTO.StudentSearchResponseDTO;
import com.example.StudentManagement.DTO.StudentUpdateRequestDTO;
import com.example.StudentManagement.Entity.Student;
import com.example.StudentManagement.Service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // CREATE

    @PostMapping public StudentResponseDTO createAdmission(
            @Valid @RequestBody StudentCreateRequestDTO request ) {
        return studentService.createStudentAdmission(request);
    }
    // UPDATE

    @PutMapping("/{studentId}")
    public StudentResponseDTO updateStudent(
            @PathVariable String studentId,
            @Valid  @RequestBody StudentUpdateRequestDTO request
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

    @GetMapping("/{studentId}/pdf")
    public ResponseEntity<byte[]> downloadStudentPdf(
            @PathVariable String studentId
    ) {

        byte[] pdf =
                studentService.generateStudentPdf(
                        studentId
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename="
                                + studentId
                                + "_Student_Profile.pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(pdf);
    }

    @GetMapping("/search")
    public StudentSearchResponseDTO searchStudent(
            @RequestParam String studentId
    ) {

        return studentService.searchByStudentId(
                studentId
        );
    }
}