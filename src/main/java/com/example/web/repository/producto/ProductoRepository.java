package com.example.web.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.Producto.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {}