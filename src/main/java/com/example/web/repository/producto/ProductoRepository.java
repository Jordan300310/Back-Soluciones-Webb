package com.example.web.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.web.models.Producto.Producto;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByEstadoTrue();
    List<Producto> findByEstadoTrueAndPrecioGreaterThan(BigDecimal precio);
    List<Producto> findByStockLessThanAndEstadoTrueOrderByStockAsc(Integer stockLimite);
    @Query("SELECT p FROM Producto p " +
           "WHERE p.stock > 0 AND p.estado = true " +
           "AND p.id NOT IN (" +
           "   SELECT vi.producto.id FROM VentaItem vi WHERE vi.venta.fechaVenta > :fechaLimite" +
           ")")
    List<Producto> findProductosSinVentasDesde(@Param("fechaLimite") LocalDateTime fechaLimite);

    @Query("SELECT vi.producto FROM VentaItem vi " +
           "GROUP BY vi.producto " +
           "ORDER BY SUM(vi.cantidad) DESC")
        List<Producto> findProductosMasVendidos(Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.estado = true " +
           "AND (:texto IS NULL OR LOWER(p.nombre) LIKE :texto OR LOWER(p.descripcion) LIKE :texto) " +
           "AND (:marcas IS NULL OR p.marca IN :marcas) " +
           "AND (:categorias IS NULL OR p.categoria IN :categorias) " +
           "AND (:min IS NULL OR p.precio >= :min) " +
           "AND (:max IS NULL OR p.precio <= :max)")
    List<Producto> filtrarProductos(
            @Param("texto") String texto,
            @Param("marcas") List<String> marcas,
            @Param("categorias") List<String> categorias,
            @Param("min") BigDecimal min,
            @Param("max") BigDecimal max
    );
}