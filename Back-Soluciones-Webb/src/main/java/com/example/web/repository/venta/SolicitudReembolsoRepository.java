package com.example.web.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.web.models.venta.SolicitudReembolso;

import java.util.List;
import java.util.Optional;

public interface SolicitudReembolsoRepository extends JpaRepository<SolicitudReembolso, Long> {

    List<SolicitudReembolso> findByVentaId(Long ventaId);

    List<SolicitudReembolso> findByClienteIdOrderByFechaSolicitudDesc(Long clienteId);

    List<SolicitudReembolso> findByEstadoOrderByFechaSolicitudDesc(String estado);

    @Query("SELECT s FROM SolicitudReembolso s WHERE s.venta.id = :ventaId AND s.estado IN ('PENDIENTE', 'APROBADO')")
    Optional<SolicitudReembolso> findSolicitudActivaByVentaId(@Param("ventaId") Long ventaId);

    List<SolicitudReembolso> findByEstado(String estado);
}
