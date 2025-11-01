package com.example.web.dto.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ComprobanteDTO {
    private Long ventaId;
    private String serie;
    private String numero;
    private LocalDateTime fechaEmision;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private String clienteNombre;

    public ComprobanteDTO(Long ventaId, String serie, String numero, 
                          LocalDateTime fecha, BigDecimal subtotal, 
                          BigDecimal igv, BigDecimal total, String clienteNombre) {
        this.ventaId = ventaId;
        this.serie = serie;
        this.numero = numero;
        this.fechaEmision = fecha;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.clienteNombre = clienteNombre;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    
}
