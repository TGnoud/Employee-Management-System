package com.example.employee_management.service;

import org.springframework.stereotype.Service;

@Service
public class UtilityService {
    public String formatName(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }
        return name.trim().toUpperCase();
    }
    public String generateEmployeeCode(Long id) {
        return String.format("EMP%04d", id);
    }
}
