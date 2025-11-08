package com.example.web.service.auth;

import com.example.web.dto.auth.MeResponse;
import com.example.web.models.auth.Empleado;
import com.example.web.repository.auth.EmpleadoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GuardService {

  private final JwtService jwtService;
  private final EmpleadoRepository empleadoRepo;

  public GuardService(JwtService jwtService,
                      EmpleadoRepository empleadoRepo) {
    this.jwtService = jwtService;
    this.empleadoRepo = empleadoRepo;
  }

  private MeResponse parseAuthHeader(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer "))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");
    String token = authHeader.substring(7);
    return jwtService.parseToken(token);
  }

  public MeResponse requireUser(String authHeader) {
    return parseAuthHeader(authHeader);
  }

  /** Debe ser EMPLEADO (o ADMIN). */
  public Empleado requireEmpleado(String authHeader) {
    MeResponse me = parseAuthHeader(authHeader);
    if (me.roles() == null || !me.roles().contains("EMPLEADO")) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo empleados");
    }
    return empleadoRepo.findByIdUsuario(me.idUsuario())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Empleado no encontrado"));
  }

  /** Debe ser ADMIN (Empleado con cargo ADMIN). */
  public Empleado requireAdmin(String authHeader) {
    Empleado emp = requireEmpleado(authHeader);
    if (emp.getCargo() == null || !emp.getCargo().equalsIgnoreCase("ADMIN")) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo administradores");
    }
    return emp;
  }

  public boolean isAdmin(String authHeader) {
    try {
      requireAdmin(authHeader);
      return true;
    } catch (ResponseStatusException ex) {
      return false;
    }
  }

  /** Debe ser CLIENTE. */
  public MeResponse requireCliente(String authHeader) {
    MeResponse me = parseAuthHeader(authHeader);
    if (me.roles() == null || !me.roles().contains("CLIENTE")) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo clientes");
    }
    return me;
  }
}
