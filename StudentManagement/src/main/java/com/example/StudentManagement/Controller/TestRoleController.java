package com.example.StudentManagement.Controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRoleController {

    @GetMapping("/admin/test")
    public String adminTest() {
        return "Admin API Working";
    }

    @GetMapping("/student/test")
    public String studentTest() {
        return "Student API Working";
    }
}