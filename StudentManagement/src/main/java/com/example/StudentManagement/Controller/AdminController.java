package com.example.StudentManagement.Controller;



import com.example.StudentManagement.DTO.DashboardResponseDTO;
import com.example.StudentManagement.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public DashboardResponseDTO dashboard() {

        return adminService.getDashboard();
    }
}