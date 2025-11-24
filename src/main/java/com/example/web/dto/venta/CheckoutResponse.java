package com.example.web.dto.venta;

import java.math.BigDecimal;

public class CheckoutResponse {
    private String initPoint; // URL de pago de Mercado Pago
    private String preferenceId; // ID de la preferencia de Mercado Pago
    private BigDecimal total;
    private Long checkoutPendienteId; // ID del checkout pendiente

    public CheckoutResponse() {
    }

    public CheckoutResponse(String initPoint, String preferenceId, BigDecimal total, Long checkoutPendienteId) {
        this.initPoint = initPoint;
        this.preferenceId = preferenceId;
        this.total = total;
        this.checkoutPendienteId = checkoutPendienteId;
    }

    public String getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(String initPoint) {
        this.initPoint = initPoint;
    }

    public String getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(String preferenceId) {
        this.preferenceId = preferenceId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getCheckoutPendienteId() {
        return checkoutPendienteId;
    }

    public void setCheckoutPendienteId(Long checkoutPendienteId) {
        this.checkoutPendienteId = checkoutPendienteId;
    }
}
