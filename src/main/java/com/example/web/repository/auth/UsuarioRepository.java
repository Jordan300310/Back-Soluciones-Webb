package com.example.web.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.web.models.auth.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findByUsernameIgnoreCase(String username);
}