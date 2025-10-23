package com.example.web.repository.auth;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.web.models.auth.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
  boolean existsByIdUsuario(Long idUsuario);
}