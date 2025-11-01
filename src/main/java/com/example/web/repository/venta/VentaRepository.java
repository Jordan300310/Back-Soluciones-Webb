package com.example.web.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.venta.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {

}
