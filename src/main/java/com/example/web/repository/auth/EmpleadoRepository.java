package com.example.web.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.web.models.auth.Empleado;
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
  boolean existsByIdUsuario(Long idUsuario);
  Optional<Empleado> findByIdUsuario(Long idUsuario);
}