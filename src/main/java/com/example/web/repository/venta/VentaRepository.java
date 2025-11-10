package com.example.web.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.venta.Venta;
import java.util.List;
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByClienteIdOrderByFechaVentaDesc(Long clienteId);

}
