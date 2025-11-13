package com.example.web.dto.admin;

import java.math.BigDecimal;
import java.util.List;

public class DashboardResponse {
    private Long ventasMes;
    private BigDecimal gananciasMes;
    private List<TopProductoDTO> topProductos;

    public DashboardResponse() {}

    public DashboardResponse(Long ventasMes, BigDecimal gananciasMes, List<TopProductoDTO> topProductos) {
        this.ventasMes = ventasMes;
        this.gananciasMes = gananciasMes;
        this.topProductos = topProductos;
    }

    public Long getVentasMes() {
        return ventasMes;
    }

    public BigDecimal getGananciasMes() {
        return gananciasMes;
    }

    public List<TopProductoDTO> getTopProductos() {
        return topProductos;
    }

    public void setVentasMes(Long ventasMes) {
        this.ventasMes = ventasMes;
    }

    public void setGananciasMes(BigDecimal gananciasMes) {
        this.gananciasMes = gananciasMes;
    }

    public void setTopProductos(List<TopProductoDTO> topProductos) {
        this.topProductos = topProductos;
    }
}
