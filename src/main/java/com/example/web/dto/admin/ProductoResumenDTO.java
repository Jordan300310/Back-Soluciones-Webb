package com.example.web.dto.admin;
import java.math.BigDecimal;

public class ProductoResumenDTO {
    private Long id;
    private String nombre;
    private Integer stock;
    private BigDecimal precio;

    public ProductoResumenDTO(Long id, String nombre, Integer stock, BigDecimal precio) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
    }
    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Integer getStock() { return stock; }
    public BigDecimal getPrecio() { return precio; }
}