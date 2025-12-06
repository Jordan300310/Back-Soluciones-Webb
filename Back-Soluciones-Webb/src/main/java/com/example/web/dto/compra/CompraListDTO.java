package com.example.web.dto.compra;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CompraListDTO(
  Long idCompra,
  Long idProveedor,
  String proveedor,      
  LocalDateTime fecha,      
  BigDecimal subtotal,
  BigDecimal igv,
  BigDecimal total,
  String productos
) {}