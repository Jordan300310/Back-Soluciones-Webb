package com.example.web.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.venta.VentaItem;
import java.util.List;

public interface VentaItemRepository extends JpaRepository<VentaItem, Long> {
    List<VentaItem> findByVentaClienteIdOrderByVentaFechaVentaDesc(Long clienteId);

}
