package com.example.web.dto.admin;
import java.math.BigDecimal;

public class VentaCategoriaDTO {
    private String categoria;
    private BigDecimal totalVenta;

    public VentaCategoriaDTO(String categoria, BigDecimal totalVenta) {
        this.categoria = categoria;
        this.totalVenta = totalVenta;
    }
    public String getCategoria() { return categoria; }
    public BigDecimal getTotalVenta() { return totalVenta; }
}