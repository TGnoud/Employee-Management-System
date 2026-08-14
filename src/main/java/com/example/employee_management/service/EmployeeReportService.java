package com.example.employee_management.service;

import com.example.employee_management.dto.DepartmentEmployeeCount;
import com.example.employee_management.repository.EmployeeRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeReportService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeReportService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeReportService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Cacheable(value = "employeeReport", key = "'totalEmployees'")
    public long getTotalEmployees() {
        logger.info("Counting total employees from database");
        return employeeRepository.count();
    }

    @Cacheable(value = "employeeReport", key = "'employeesByDepartment'")
    public List<DepartmentEmployeeCount> getEmployeesByDepartment() {
        logger.info("Counting employees by department from database");
        return employeeRepository.countEmployeesByDepartment();
    }
}
