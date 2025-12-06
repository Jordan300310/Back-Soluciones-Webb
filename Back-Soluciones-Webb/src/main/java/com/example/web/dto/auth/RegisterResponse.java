package com.example.web.dto.auth;

public record RegisterResponse(
    String message,
    String username,
    String rol   // "CLIENTE"
) {}
