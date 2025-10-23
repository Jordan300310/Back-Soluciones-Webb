package com.example.web.dto.compra;

import java.util.List;

public record CompraRequest(
  Long idProveedor,
  List<CompraItemRequest> items
) {}