package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.exception.BadRequestException;
import com.example.employee_management.model.Department;
import com.example.employee_management.model.Employee;
import com.example.employee_management.repository.DepartmentRepository;
import com.example.employee_management.service.EmployeeService;
import com.example.employee_management.service.UtilityService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository;
    private final UtilityService utilityService;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(
            EmployeeService employeeService,
            DepartmentRepository departmentRepository,
            UtilityService utilityService,
            PasswordEncoder passwordEncoder
    ) {
        this.employeeService = employeeService;
        this.departmentRepository = departmentRepository;
        this.utilityService = utilityService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId
    ) {
        return ResponseEntity.ok(employeeService.getEmployees(keyword, departmentId));
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody EmployeeRequest request,
            BindingResult bindingResult
    ) {
        validateRequest(bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(request));
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request,
            BindingResult bindingResult
    ) {
        validateRequest(bindingResult);
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentRepository.save(department));
    }

    @GetMapping("/employees/format-name")
    public String formatName(@RequestParam String name) {
        return utilityService.formatName(name);
    }

    @GetMapping("/employees/generate-code")
    public String generateEmployeeCode(@RequestParam Long id) {
        return utilityService.generateEmployeeCode(id);
    }

    @GetMapping("/password/encode")
    public String encodePassword(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }

    private void validateRequest(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return;
        }

        Map<String, String> fieldErrors = bindingResult.getFieldErrors()
                .stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                FieldError::getField,
                                fieldError -> fieldError.getDefaultMessage() == null
                                        ? "Du lieu khong hop le"
                                        : fieldError.getDefaultMessage(),
                                (first, second) -> first
                        )
                );

        throw new BadRequestException("Du lieu request khong hop le", fieldErrors);
    }
}
