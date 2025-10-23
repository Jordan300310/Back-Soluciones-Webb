package com.example.web.repository.proveedor;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.Proveedor.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {}