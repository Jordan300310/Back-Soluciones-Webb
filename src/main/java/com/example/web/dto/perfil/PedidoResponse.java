package com.example.web.dto.perfil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.web.models.venta.Comprobante;
import com.example.web.models.venta.Venta;

public record PedidoResponse(
    Long idVenta,
    String numeroComprobante,
    LocalDateTime fechaVenta,
    String direccion,
    String ciudad,
    String pais,
    BigDecimal total,
    List<PedidoProductoResponse> productos
) {
  public static PedidoResponse from(Venta v) {
    BigDecimal total = null;
    Comprobante comp = v.getComprobante();
    if (comp != null && comp.getTotal() != null) {
      total = comp.getTotal();
    } else if (v.getTotal() != null) {
      total = v.getTotal();
    }

    String numeroComprobante = null;
    if (comp != null) {
      numeroComprobante = comp.getSerie() + "-" + comp.getNumero();
    }

    List<PedidoProductoResponse> productos = v.getItems().stream()
        .map(PedidoProductoResponse::from)
        .toList();

    return new PedidoResponse(
        v.getId(),
        numeroComprobante,
        v.getFechaVenta(),
        v.getDireccion(),
        v.getCiudad(),
        v.getPais(),
        total,
        productos
    );
  }
}