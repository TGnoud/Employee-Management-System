package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.exception.BadRequestException;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.model.Department;
import com.example.employee_management.model.Employee;
import com.example.employee_management.repository.DepartmentRepository;
import com.example.employee_management.repository.EmployeeRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UtilityService utilityService;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            UtilityService utilityService
    ) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.utilityService = utilityService;
    }

    public List<Employee> getEmployees(String keyword, Long departmentId) {
        if (keyword != null && !keyword.isBlank()) {
            return employeeRepository.searchByNameOrDepartment(keyword);
        }

        if (departmentId != null) {
            return employeeRepository.findByDepartmentId(departmentId);
        }

        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @CacheEvict(value = "employeeReport", allEntries = true)
    public Employee createEmployee(EmployeeRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new BadRequestException(
                        "Khong tim thay phong ban voi id = " + request.getDepartmentId()
                ));

        Employee employee = new Employee();
        employee.setName(utilityService.formatName(request.getName()));
        employee.setEmail(request.getEmail());
        employee.setDepartment(department);

        Employee savedEmployee = employeeRepository.save(employee);
        logger.info(
                "Created employee id={}, email={}, departmentId={}",
                savedEmployee.getId(),
                savedEmployee.getEmail(),
                department.getId()
        );

        return savedEmployee;
    }

    @CacheEvict(value = "employeeReport", allEntries = true)
    public Employee updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new BadRequestException(
                        "Khong tim thay phong ban voi id = " + request.getDepartmentId()
                ));

        employee.setName(utilityService.formatName(request.getName()));
        employee.setEmail(request.getEmail());
        employee.setDepartment(department);

        Employee updatedEmployee = employeeRepository.save(employee);
        logger.info(
                "Updated employee id={}, email={}, departmentId={}",
                updatedEmployee.getId(),
                updatedEmployee.getEmail(),
                department.getId()
        );

        return updatedEmployee;
    }

    @CacheEvict(value = "employeeReport", allEntries = true)
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }

        employeeRepository.deleteById(id);
        logger.info("Deleted employee id={}", id);
    }
}
