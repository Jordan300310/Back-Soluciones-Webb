package com.example.web.dto.admin;
import com.example.web.models.auth.Usuario;
import com.example.web.models.auth.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmpleadoAdminDTO(
  Long idEmpleado,
  Long idUsuario,
  String username,
  String password,
  Boolean usuarioEstado,

  String nom,
  String apat,
  String amat,
  String dni,
  String cel,
  String email,
  LocalDate fen,
  String cargo,
  BigDecimal sueldo,
  Boolean empleadoEstado
) {
  public static EmpleadoAdminDTO of(Empleado e, Usuario u) {
    return new EmpleadoAdminDTO(
      e.getId(),
      e.getIdUsuario(),
      u != null ? u.getUsername() : null,
      u != null ? u.getPassword() : null,
      u != null ? u.getEstado()   : null,
      e.getNom(), e.getApat(), e.getAmat(),
      e.getDni(), e.getCel(), e.getEmail(),
      e.getFen(), e.getCargo(), e.getSueldo(),
      e.getEstado()
    );
  }
}
