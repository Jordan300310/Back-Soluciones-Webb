package com.example.web.dto.compra;

import java.math.BigDecimal;

public record CompraCreatedDTO(
  Long idCompra,
  BigDecimal subtotal,
  BigDecimal igv,
  BigDecimal total
) {}