package com.example.web.models.Proveedor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "proveedor")
@Getter @Setter @NoArgsConstructor
public class Proveedor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_proveedor")
  private Long id;

  @Column(name = "razon_social", length = 150)
  private String razonSocial;

  @Column(name = "ruc", length = 15, unique = true)
  private String ruc;

  @Column(name = "cel", length = 20)
  private String cel;

  @Column(name = "email", length = 150)
  private String email;

  @Column(nullable = false)
  private Boolean estado = true;
}
