package com.example.web.repository.venta;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.web.models.venta.VentaItem;
import java.util.List;
import java.time.LocalDateTime;

public interface VentaItemRepository extends JpaRepository<VentaItem, Long> {
    List<VentaItem> findByVentaClienteIdOrderByVentaFechaVentaDesc(Long clienteId);

    @Query("SELECT vi.producto.id, vi.producto.nombre, SUM(vi.cantidad), SUM(vi.precioUnitario * vi.cantidad) " +
           "FROM VentaItem vi " +
           "WHERE vi.venta.fechaVenta BETWEEN :start AND :end " +
           "GROUP BY vi.producto.id, vi.producto.nombre " +
           "ORDER BY SUM(vi.cantidad) DESC")
    List<Object[]> findTopProductosBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

}
