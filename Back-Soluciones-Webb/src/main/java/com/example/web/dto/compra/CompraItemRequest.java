package com.example.web.dto.compra;

import java.math.BigDecimal;

public record CompraItemRequest(
  Long idProducto,
  Integer cantidad,
  BigDecimal costoUnit
) {}