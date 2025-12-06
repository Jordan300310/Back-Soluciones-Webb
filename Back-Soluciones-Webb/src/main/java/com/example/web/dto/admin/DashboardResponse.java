package com.example.web.dto.admin;

import java.math.BigDecimal;
import java.util.List;

public class DashboardResponse {
    // Métricas Escalares
    private Long ventasMes;
    private BigDecimal gananciasMes;

    // Listas / Gráficos
    private List<TopProductoDTO> topProductos;
    
    // ESTE es el campo genérico que usaremos para la línea de tiempo (sea mes o rango)
    // Reemplaza a 'ventasUltimos7Dias'
    private List<VentaDiariaDTO> ventasPorPeriodo; 
    
    private List<VentaCategoriaDTO> ventasPorCategoria;
    private List<ProductoResumenDTO> productosBajoStock;
    private List<ProductoResumenDTO> productosSinVentas; 

    public DashboardResponse() {}

    public DashboardResponse(Long ventasMes, BigDecimal gananciasMes, List<TopProductoDTO> topProductos) {
        this.ventasMes = ventasMes;
        this.gananciasMes = gananciasMes;
        this.topProductos = topProductos;
    }

    // --- Getters y Setters ---

    public Long getVentasMes() {
        return ventasMes;
    }

    public void setVentasMes(Long ventasMes) {
        this.ventasMes = ventasMes;
    }

    public BigDecimal getGananciasMes() {
        return gananciasMes;
    }

    public void setGananciasMes(BigDecimal gananciasMes) {
        this.gananciasMes = gananciasMes;
    }

    public List<TopProductoDTO> getTopProductos() {
        return topProductos;
    }

    public void setTopProductos(List<TopProductoDTO> topProductos) {
        this.topProductos = topProductos;
    }

    // Getter/Setter para la gráfica de línea
    public List<VentaDiariaDTO> getVentasPorPeriodo() { 
        return ventasPorPeriodo; 
    }
    
    public void setVentasPorPeriodo(List<VentaDiariaDTO> ventasPorPeriodo) { 
        this.ventasPorPeriodo = ventasPorPeriodo; 
    }

    public List<VentaCategoriaDTO> getVentasPorCategoria() { return ventasPorCategoria; }
    public void setVentasPorCategoria(List<VentaCategoriaDTO> ventasPorCategoria) { this.ventasPorCategoria = ventasPorCategoria; }

    public List<ProductoResumenDTO> getProductosBajoStock() { return productosBajoStock; }
    public void setProductosBajoStock(List<ProductoResumenDTO> productosBajoStock) { this.productosBajoStock = productosBajoStock; }

    public List<ProductoResumenDTO> getProductosSinVentas() { return productosSinVentas; }
    public void setProductosSinVentas(List<ProductoResumenDTO> productosSinVentas) { this.productosSinVentas = productosSinVentas; }
}
