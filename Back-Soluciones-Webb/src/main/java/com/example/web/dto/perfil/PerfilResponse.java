package com.example.web.dto.perfil;

import java.time.LocalDate;

import com.example.web.models.auth.Cliente;
import com.example.web.models.auth.Usuario;

public record PerfilResponse(
    String username,
    String nombres,
    String apellidoPaterno,
    String apellidoMaterno,
    String dni,
    String celular,
    String email,
    LocalDate fechaNacimiento
) {
  public static PerfilResponse from(Usuario u, Cliente c) {
    if (u == null || c == null) return null;

    return new PerfilResponse(
        u.getUsername(),
        c.getNom(),
        c.getApat(),
        c.getAmat(),
        c.getDni(),
        c.getCel(),
        c.getEmail(),
        c.getFen()
    );
  }
}