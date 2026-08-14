package com.example.employee_management.dto.auth;

public record AuthResponse(
        String token,
        String tokenType,
        String username,
        String role
) {
}
