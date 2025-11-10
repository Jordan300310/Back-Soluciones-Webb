package com.example.web.models.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cliente")
@Getter @Setter @NoArgsConstructor
public class Cliente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_cliente")
  private Long id;

  @Column(name = "id_usuario", unique = true)
  private Long idUsuario;

  @Column(length = 150) private String nom;   // nombres
  @Column(length = 100) private String apat;  // apellido paterno
  @Column(length = 100) private String amat;  // apellido materno
  @Column(length = 15, unique = true) private String dni;
  @Column(length = 20) private String cel;
  @Column(length = 150) private String email;
  private LocalDate fen;                     // fecha nacimiento


  @Column(nullable = false)
  private Boolean estado = true;
}