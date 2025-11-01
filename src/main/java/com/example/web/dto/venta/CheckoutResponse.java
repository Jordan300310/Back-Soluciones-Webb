package com.example.web.dto.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CheckoutResponse {
    private Long ventaId;
    private String serieComprobante;
    private String numeroComprobante;
    private LocalDateTime fechaEmision;
    private BigDecimal total;

    public CheckoutResponse(Long ventaId, String serie, String numero, LocalDateTime fecha, BigDecimal total) {
        this.ventaId = ventaId;
        this.serieComprobante = serie;
        this.numeroComprobante = numero;
        this.fechaEmision = fecha;
        this.total = total;
    }

    public Long getVentaId() {
        return ventaId;
    }
    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }
    public String getSerieComprobante() {
        return serieComprobante;
    }
    public void setSerieComprobante(String serieComprobante) {
        this.serieComprobante = serieComprobante;
    }
    public String getNumeroComprobante() {
        return numeroComprobante;
    }
    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }
    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
