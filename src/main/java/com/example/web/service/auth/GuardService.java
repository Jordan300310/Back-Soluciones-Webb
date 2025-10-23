package com.example.web.service.auth;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.web.models.auth.Empleado;
import com.example.web.models.auth.SessionUser;
import com.example.web.repository.auth.EmpleadoRepository;

@Service
public class GuardService {

  private final EmpleadoRepository empleadoRepo;

  public GuardService(EmpleadoRepository empleadoRepo) {
    this.empleadoRepo = empleadoRepo;
  }

  public SessionUser requireSesion(HttpSession session) {
    Object o = session.getAttribute(AuthService.SESSION_KEY);
    if (o == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Inicia sesión");
    return (SessionUser) o;
  }

  /** Debe ser EMPLEADO (o ADMIN). */
  public Empleado requireEmpleado(HttpSession session) {
    SessionUser su = requireSesion(session);
    if (su.getRoles() == null || !su.getRoles().contains("EMPLEADO")) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo empleados");
    }
    return empleadoRepo.findByIdUsuario(su.getIdUsuario())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Empleado no encontrado"));
  }

  /** Debe ser ADMIN (Empleado con cargo ADMIN). */
  public Empleado requireAdmin(HttpSession session) {
    Empleado emp = requireEmpleado(session);
    if (emp.getCargo() == null || !emp.getCargo().equalsIgnoreCase("ADMIN")) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo administradores");
    }
    return emp;
  }

  /** true si es ADMIN, false si solo EMPLEADO. */
  public boolean isAdmin(HttpSession session) {
    try {
      requireAdmin(session);
      return true;
    } catch (ResponseStatusException ex) {
      return false;
    }
  }
}