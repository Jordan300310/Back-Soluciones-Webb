package com.example.web.models.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "empleado")
@Getter @Setter @NoArgsConstructor
public class Empleado {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_empleado")
  private Long id;

  @Column(name = "id_usuario", unique = true)
  private Long idUsuario;

  @Column(length = 150) private String nom;
  @Column(length = 100) private String apat;
  @Column(length = 100) private String amat;
  @Column(length = 15, unique = true) private String dni;
  @Column(length = 20) private String cel;
  @Column(length = 150) private String email;
  private LocalDate fen;

  @Column(length = 100) private String cargo;
  @Column(precision = 10, scale = 2) private BigDecimal sueldo;

  @Column(nullable = false)
  private Boolean estado = true;
}