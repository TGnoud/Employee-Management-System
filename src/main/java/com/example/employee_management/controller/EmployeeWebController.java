package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.exception.BadRequestException;
import com.example.employee_management.model.Department;
import com.example.employee_management.repository.DepartmentRepository;
import com.example.employee_management.service.EmployeeService;
import com.example.employee_management.service.EmployeeReportService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmployeeWebController {
    private final EmployeeService employeeService;
    private final EmployeeReportService employeeReportService;
    private final DepartmentRepository departmentRepository;

    public EmployeeWebController(
            EmployeeService employeeService,
            EmployeeReportService employeeReportService,
            DepartmentRepository departmentRepository
    ) {
        this.employeeService = employeeService;
        this.employeeReportService = employeeReportService;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/employees/list")
    public String listEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId,
            Model model
    ) {
        model.addAttribute("employees", employeeService.getEmployees(keyword, departmentId));
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("departmentId", departmentId);
        return "employees/list";
    }

    @GetMapping("/employees/statistics")
    public String showStatistics(Model model) {
        model.addAttribute("totalEmployees", employeeReportService.getTotalEmployees());
        model.addAttribute("departmentStatistics", employeeReportService.getEmployeesByDepartment());
        return "employees/statistics";
    }

    @GetMapping("/employees/add")
    public String showAddForm(Model model) {
        model.addAttribute("employeeRequest", new EmployeeRequest());
        model.addAttribute("departments", departmentRepository.findAll());
        return "employees/add";
    }

    @PostMapping("/employees/add")
    public String addEmployee(
            @Valid @ModelAttribute("employeeRequest") EmployeeRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentRepository.findAll());
            return "employees/add";
        }

        try {
            employeeService.createEmployee(request);
        } catch (BadRequestException exception) {
            bindingResult.rejectValue("departmentId", "department.notFound", exception.getMessage());
            model.addAttribute("departments", departmentRepository.findAll());
            return "employees/add";
        }

        return "redirect:/employees/list";
    }

    @PostMapping("/employees/departments")
    public String addDepartment(@RequestParam String name) {
        if (name != null && !name.isBlank()) {
            departmentRepository.save(new Department(null, name.trim()));
        }

        return "redirect:/employees/add";
    }
}
