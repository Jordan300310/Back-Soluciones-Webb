package com.example.web.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.web.models.Producto.Producto;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByEstadoTrue();
    // 1. Alerta Stock Bajo (ej: menos de 10) y que esté activo
    List<Producto> findByStockLessThanAndEstadoTrueOrderByStockAsc(Integer stockLimite);

    // 2. Productos Zombies: Tienen stock pero no se han vendido desde X fecha
    @Query("SELECT p FROM Producto p " +
           "WHERE p.stock > 0 AND p.estado = true " +
           "AND p.id NOT IN (" +
           "   SELECT vi.producto.id FROM VentaItem vi WHERE vi.venta.fechaVenta > :fechaLimite" +
           ")")
    List<Producto> findProductosSinVentasDesde(@Param("fechaLimite") LocalDateTime fechaLimite);
}