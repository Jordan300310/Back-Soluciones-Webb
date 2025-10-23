package com.example.web.models.Producto;
import com.example.web.models.Proveedor.Proveedor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "producto")
@Getter @Setter @NoArgsConstructor
public class Producto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_producto")
  private Long id;

   @Column(name = "nombre_producto", nullable = false, length = 150)
  private String nombre;

  @Column(length = 500)
  private String descripcion;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal precio;

  @Column(nullable = false)
  private Integer stock;

  // En tu diseño: texto simple
  @Column(length = 100)
  private String marca;

  @Column(length = 100)
  private String categoria;

  // Relación con proveedor por id (simple)
  @Column(name = "id_proveedor")
  private Long idProveedor;

  @Column(name = "imagen_url", length = 500)
  private String imagenUrl;

  @Column(nullable = false)
  private Boolean estado = true;

  // solo para leer nombre del proveedor sin manejar la relación
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_proveedor", insertable = false, updatable = false)
  private Proveedor proveedor;
}