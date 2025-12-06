package com.example.web.dto.auth;

import java.util.List;

public record MeResponse(
    Long idUsuario,
    String username,
    List<String> roles,
    boolean enabled
) {}
