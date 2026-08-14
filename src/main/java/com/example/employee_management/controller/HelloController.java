package com.example.employee_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Employee Management System";
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello Employee Management System!";
    }
}