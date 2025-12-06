package com.example.web.dto.auth;

import java.util.List;

public record LoginResponse(
    String message,
    List<String> roles,
    String token
) {}
