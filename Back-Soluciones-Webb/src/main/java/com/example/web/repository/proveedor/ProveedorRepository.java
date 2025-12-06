package com.example.web.repository.proveedor;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.Proveedor.Proveedor;
import java.util.List;
import java.util.Optional;
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
     List<Proveedor> findByEstadoTrue();
     Optional<Proveedor> findByRuc(String ruc); 
}