package com.example.web.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.venta.Comprobante;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {

}
