package com.example.web.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.web.models.auth.Empleado;
import java.util.List;
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
  List<Empleado> findByEstadoTrue();
  boolean existsByIdUsuario(Long idUsuario);
  Optional<Empleado> findByIdUsuario(Long idUsuario);
}