package com.example.web.repository.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.auth.Cliente;
import java.util.List;
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
  boolean existsByIdUsuario(Long idUsuario);
  boolean existsByDni(String dni);
  boolean existsByEmailIgnoreCase(String email);
  Optional<Cliente> findByIdUsuario(Long idUsuario);
  List<Cliente> findByEstadoTrue();
}