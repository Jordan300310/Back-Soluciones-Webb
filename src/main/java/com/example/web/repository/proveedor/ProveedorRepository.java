package com.example.web.repository.proveedor;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.Proveedor.Proveedor;
import java.util.List;
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
     List<Proveedor> findByEstadoTrue();
}