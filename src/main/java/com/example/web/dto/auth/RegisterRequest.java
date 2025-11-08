package com.example.web.dto.auth;

import java.time.LocalDate;

public record RegisterRequest(
    String nombres,
    String apat,
    String amat,
    String dni,
    String cel,
    String email,
    LocalDate fen,   // "yyyy-MM-dd"
    String username,
    String password
) {}
