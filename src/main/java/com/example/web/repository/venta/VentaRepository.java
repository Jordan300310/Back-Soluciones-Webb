package com.example.web.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.web.models.venta.Venta;
import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByClienteIdOrderByFechaVentaDesc(Long clienteId);

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fechaVenta BETWEEN :start AND :end")
    BigDecimal sumTotalBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(v) FROM Venta v WHERE v.fechaVenta BETWEEN :start AND :end")
    Long countByFechaVentaBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
