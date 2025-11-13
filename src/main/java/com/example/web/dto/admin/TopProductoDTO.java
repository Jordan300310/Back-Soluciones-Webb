package com.example.web.dto.admin;

import java.math.BigDecimal;

public class TopProductoDTO {
    private Long productoId;
    private String nombre;
    private Long cantidadVendida;
    private BigDecimal totalVenta;

    public TopProductoDTO() {}

    public TopProductoDTO(Long productoId, String nombre, Long cantidadVendida, BigDecimal totalVenta) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.cantidadVendida = cantidadVendida;
        this.totalVenta = totalVenta;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getCantidadVendida() {
        return cantidadVendida;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidadVendida(Long cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }
}
