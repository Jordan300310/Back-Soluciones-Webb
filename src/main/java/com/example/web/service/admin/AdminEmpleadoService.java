package com.example.web.service.admin;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.dto.admin.CrearEmpleadoRequest;
import com.example.web.dto.admin.EmpleadoAdminDTO;
import com.example.web.dto.admin.UpdateEmpleadoRequest;
import com.example.web.models.auth.Empleado;
import com.example.web.models.auth.Usuario;
import com.example.web.repository.auth.EmpleadoRepository;
import com.example.web.repository.auth.UsuarioRepository;
import java.util.Objects;
import java.util.List;
import java.time.LocalDate;
@Service
public class AdminEmpleadoService {

  private final EmpleadoRepository empleadoRepo;
  private final UsuarioRepository usuarioRepo;
  private final PasswordEncoder passwordEncoder;

  public AdminEmpleadoService(EmpleadoRepository empleadoRepo,
                              UsuarioRepository usuarioRepo,
                              PasswordEncoder passwordEncoder) {
    this.empleadoRepo = empleadoRepo;
    this.usuarioRepo = usuarioRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
public EmpleadoAdminDTO crear(CrearEmpleadoRequest r) {
  if (r.username() == null || r.username().isBlank())
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username requerido");
  if (usuarioRepo.findByUsernameIgnoreCase(r.username()).isPresent())
    throw new ResponseStatusException(HttpStatus.CONFLICT, "Username ya registrado");

  if (r.password() == null || r.password().isBlank())
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password requerida");
  if (r.fen() != null && r.fen().isAfter(LocalDate.now())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de nacimiento no puede ser mayor a la fecha actual");
    }

  if (r.email() != null && !r.email().isBlank()) {
    String email = r.email().trim();
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email inválido");
    }
  }

  if (r.dni() != null && !r.dni().isBlank()) {
    String dni = r.dni().trim();
    if (empleadoRepo.findByDni(dni).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "DNI ya registrado");
    }
  }

  Usuario u = new Usuario();
  u.setUsername(r.username());
  u.setPassword(passwordEncoder.encode(r.password())); // HASH
  u.setEstado(true);
  usuarioRepo.save(u);

  Empleado e = new Empleado();
  e.setIdUsuario(u.getId());
  e.setNom(r.nom());
  e.setApat(r.apat());
  e.setAmat(r.amat());
  e.setDni(r.dni());
  e.setCel(r.cel());
  e.setEmail(r.email());
  e.setFen(r.fen());
  e.setCargo(r.cargo());
  e.setSueldo(r.sueldo());
  e.setEstado(true);
  empleadoRepo.save(e);

  return EmpleadoAdminDTO.of(e, u);
}

@Transactional(readOnly = true)
public List<EmpleadoAdminDTO> list() {
  return empleadoRepo.findByEstadoTrue().stream()  
      .map(e -> {
        Long idUsuario = e.getIdUsuario();
        if (idUsuario == null) {
          return null;
        }

        return usuarioRepo
            .findByIdAndEstadoTrue(idUsuario)      
            .map(u -> EmpleadoAdminDTO.of(e, u))
            .orElse(null);
      })
      .filter(Objects::nonNull)                  
      .toList();
}

  @Transactional(readOnly = true)
  public EmpleadoAdminDTO get(Long id) {
    Empleado e = empleadoRepo.findById(id).orElse(null);
    if (e == null) return null;
    Usuario u = (e.getIdUsuario() != null)
        ? usuarioRepo.findById(e.getIdUsuario()).orElse(null)
        : null;
    return EmpleadoAdminDTO.of(e, u);
  }

  @Transactional
  public EmpleadoAdminDTO update(Long id, UpdateEmpleadoRequest in) {
    Empleado e = empleadoRepo.findById(id).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));

    if (in.nom() != null)   e.setNom(in.nom());
    if (in.apat() != null)  e.setApat(in.apat());
    if (in.amat() != null)  e.setAmat(in.amat());
    if (in.dni() != null)   e.setDni(in.dni());
    if (in.cel() != null)   e.setCel(in.cel());
    if (in.email() != null) e.setEmail(in.email());
    if (in.fen() != null)   e.setFen(in.fen());
    if (in.cargo() != null) e.setCargo(in.cargo());
    if (in.sueldo() != null)e.setSueldo(in.sueldo());

    empleadoRepo.save(e);

    Usuario u = null;
    if (in.fen() != null) {
        if (in.fen().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de nacimiento no puede ser mayor a la fecha actual");
        }
        e.setFen(in.fen());
    }
    if (e.getIdUsuario() != null) {
      u = usuarioRepo.findById(e.getIdUsuario()).orElse(null);
      if (u != null) {
        if (in.username() != null && !in.username().isBlank()) {
          var existing = usuarioRepo.findByUsernameIgnoreCase(in.username());
          if (existing.isPresent() && !existing.get().getId().equals(u.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username ya registrado");
          }
          u.setUsername(in.username());
        }
        usuarioRepo.save(u);
      }
    }

    return EmpleadoAdminDTO.of(e, u);
  }

  @Transactional
  public void delete(Long id) {
    Empleado e = empleadoRepo.findById(id).orElse(null);
    if (e != null) {
      e.setEstado(false);
      empleadoRepo.save(e);

      Long idUsuario = e.getIdUsuario();
      if (idUsuario != null) {
        Usuario u = usuarioRepo.findById(idUsuario).orElse(null);
        if (u != null) {
          u.setEstado(false);
          usuarioRepo.save(u);
        }
      }
    }
  }
}
