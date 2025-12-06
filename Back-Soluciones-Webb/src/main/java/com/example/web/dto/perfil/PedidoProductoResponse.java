package com.example.web.dto.perfil;

import java.math.BigDecimal;

import com.example.web.models.Producto.Producto;
import com.example.web.models.venta.VentaItem;

public record PedidoProductoResponse(
    String nombreProducto,
    String imagenUrl,
    Integer cantidad,
    BigDecimal precioUnitario,
    BigDecimal subtotal
) {
  public static PedidoProductoResponse from(VentaItem vi) {
    Producto p = vi.getProducto();

    BigDecimal unit = vi.getPrecioUnitario() != null
        ? vi.getPrecioUnitario()
        : (p.getPrecio() != null ? p.getPrecio() : BigDecimal.ZERO);

    BigDecimal subtotal = unit.multiply(BigDecimal.valueOf(vi.getCantidad()));

    return new PedidoProductoResponse(
        p.getNombre(),
        p.getImagenUrl(),
        vi.getCantidad(),
        unit,
        subtotal
    );
  }
}