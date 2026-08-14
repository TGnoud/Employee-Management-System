package com.example.employee_management.controller;

import com.example.employee_management.dto.DepartmentEmployeeCount;
import com.example.employee_management.service.EmployeeReportService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class EmployeeReportController {
    private final EmployeeReportService employeeReportService;

    public EmployeeReportController(EmployeeReportService employeeReportService) {
        this.employeeReportService = employeeReportService;
    }

    @GetMapping("/employees/total")
    public ResponseEntity<Map<String, Long>> getTotalEmployees() {
        return ResponseEntity.ok(Map.of("totalEmployees", employeeReportService.getTotalEmployees()));
    }

    @GetMapping("/employees/by-department")
    public ResponseEntity<List<DepartmentEmployeeCount>> getEmployeesByDepartment() {
        return ResponseEntity.ok(employeeReportService.getEmployeesByDepartment());
    }
}
