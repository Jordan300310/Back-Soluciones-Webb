package com.example.web.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateEmpleadoRequest(
  // EMPLEADO
  String nom,
  String apat,
  String amat,
  String dni,
  String cel,
  String email,
  LocalDate fen,
  String cargo,
  BigDecimal sueldo,

  // USUARIO
  String username
) {}