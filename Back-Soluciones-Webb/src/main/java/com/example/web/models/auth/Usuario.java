package com.example.web.models.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_usuario")
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false, length = 255)
  private String password; // por ahora SIN hash

  @Column(nullable = false)
  private Boolean estado = true;
}