package com.example.web.dto.perfil;

public record ChangePasswordRequest(
    String currentPassword,
    String newPassword
) {}