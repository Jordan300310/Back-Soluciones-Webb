package com.example.web.dto.admin;

import java.math.BigDecimal;
import java.util.List;

public class DashboardResponse {
    private Long ventasMes;
    private BigDecimal gananciasMes;
    private List<TopProductoDTO> topProductos;
    private List<VentaDiariaDTO> ventasUltimos7Dias;
    private List<VentaCategoriaDTO> ventasPorCategoria;
    private List<ProductoResumenDTO> productosBajoStock;
    private List<ProductoResumenDTO> productosSinVentas; 

    public DashboardResponse() {}

    public DashboardResponse(Long ventasMes, BigDecimal gananciasMes, List<TopProductoDTO> topProductos) {
        this.ventasMes = ventasMes;
        this.gananciasMes = gananciasMes;
        this.topProductos = topProductos;
    }
    public List<VentaDiariaDTO> getVentasUltimos7Dias() { return ventasUltimos7Dias; }
    public void setVentasUltimos7Dias(List<VentaDiariaDTO> ventasUltimos7Dias) { this.ventasUltimos7Dias = ventasUltimos7Dias; }

    public List<VentaCategoriaDTO> getVentasPorCategoria() { return ventasPorCategoria; }
    public void setVentasPorCategoria(List<VentaCategoriaDTO> ventasPorCategoria) { this.ventasPorCategoria = ventasPorCategoria; }

    public List<ProductoResumenDTO> getProductosBajoStock() { return productosBajoStock; }
    public void setProductosBajoStock(List<ProductoResumenDTO> productosBajoStock) { this.productosBajoStock = productosBajoStock; }

    public List<ProductoResumenDTO> getProductosSinVentas() { return productosSinVentas; }
    public void setProductosSinVentas(List<ProductoResumenDTO> productosSinVentas) { this.productosSinVentas = productosSinVentas; }

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
