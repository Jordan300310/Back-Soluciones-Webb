package com.example.web.dto.admin;

import java.time.LocalDate;

public record UpdateClienteRequest(
  // CLIENTE
  String nom,
  String apat,
  String amat,
  String dni,
  String cel,
  String email,
  LocalDate fen,
  Boolean clienteEstado,

  // USUARIO
  String username,
  String password,       // SIN hash (por ahora)
  Boolean usuarioEstado
) {}

