package com.example.web.repository.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.auth.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
  boolean existsByIdUsuario(Long idUsuario);

  Optional<Cliente> findByEmail(String email);
  Optional<Cliente> findByIdUsuario(Long idUsuario);
}