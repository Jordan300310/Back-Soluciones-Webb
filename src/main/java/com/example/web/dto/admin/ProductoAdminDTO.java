package com.example.web.dto.admin;

import com.example.web.models.Producto.Producto;

import java.math.BigDecimal;

public record ProductoAdminDTO(
  Long id,
  String nombre,
  String descripcion,
  BigDecimal precio,
  Integer stock,
  String marca,
  String categoria,
  Long idProveedor,
  String proveedorNombre,
  String imagenUrl,
  Boolean estado
) {
  public static ProductoAdminDTO of(Producto p) {
    String provName = (p.getProveedor() != null) ? p.getProveedor().getRazonSocial() : null;
    return new ProductoAdminDTO(
        p.getId(), p.getNombre(), p.getDescripcion(),
        p.getPrecio(), p.getStock(), p.getMarca(), p.getCategoria(),
        p.getIdProveedor(), provName, p.getImagenUrl(), p.getEstado()
    );
  }
}