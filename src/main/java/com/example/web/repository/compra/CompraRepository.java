package com.example.web.repository.compra;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.compra.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    
}