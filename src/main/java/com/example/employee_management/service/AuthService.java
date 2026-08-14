package com.example.employee_management.service;

import com.example.employee_management.dto.auth.AuthRequest;
import com.example.employee_management.dto.auth.AuthResponse;
import com.example.employee_management.dto.auth.RegisterRequest;
import com.example.employee_management.exception.BadRequestException;
import com.example.employee_management.model.AppUser;
import com.example.employee_management.repository.AppUserRepository;
import com.example.employee_management.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String username = request.getUsername().trim();
        if (appUserRepository.existsByUsername(username)) {
            throw new BadRequestException("Username da ton tai");
        }

        String role = normalizeRole(request.getRole());
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setRole(role);
        appUserRepository.save(appUser);

        String token = jwtService.generateToken(username, role);
        return new AuthResponse(token, "Bearer", username, role);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        AppUser appUser = appUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Sai username hoac password"));

        String token = jwtService.generateToken(appUser.getUsername(), appUser.getRole());
        return new AuthResponse(token, "Bearer", appUser.getUsername(), appUser.getRole());
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "USER";
        }

        String normalizedRole = role.trim().toUpperCase();
        if (!normalizedRole.equals("USER") && !normalizedRole.equals("ADMIN")) {
            throw new BadRequestException("Role chi duoc la USER hoac ADMIN");
        }

        return normalizedRole;
    }
}
