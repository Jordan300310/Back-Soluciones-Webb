package com.example.web.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.venta.VentaItem;

public interface VentaItemRepository extends JpaRepository<VentaItem, Long> {

}
