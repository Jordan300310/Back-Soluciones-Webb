package com.example.web.dto.admin;
import java.math.BigDecimal;

public class VentaDiariaDTO {
    private String fecha;
    private BigDecimal total;

    public VentaDiariaDTO(String fecha, BigDecimal total) {
        this.fecha = fecha;
        this.total = total;
    }
    public String getFecha() { return fecha; }
    public BigDecimal getTotal() { return total; }
}