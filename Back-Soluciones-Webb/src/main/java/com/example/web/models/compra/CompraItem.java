package com.example.web.models.compra;

import jakarta.persistence.*;
import lombok.Getter; 
import lombok.Setter; 
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity @Table(name = "compra_item")
@Getter @Setter @NoArgsConstructor
public class CompraItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_compra_item")
  private Long id;

  @Column(name = "id_compra", nullable = false)
  private Long idCompra;

  @Column(name = "id_producto", nullable = false)
  private Long idProducto;

  @Column(name = "costo_unit", nullable = false, precision = 10, scale = 2)
  private BigDecimal costoUnit;

  @Column(nullable = false)
  private Integer cantidad;

  @Column(name = "total_linea", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalLinea;
}