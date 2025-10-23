package com.example.web.service.auth;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.dto.auth.LoginRequest;
import com.example.web.dto.auth.LoginResponse;
import com.example.web.dto.auth.RegisterRequest;
import com.example.web.dto.auth.RegisterResponse;
import com.example.web.models.auth.Cliente;
import com.example.web.models.auth.SessionUser;
import com.example.web.models.auth.Usuario;
import com.example.web.repository.auth.ClienteRepository;
import com.example.web.repository.auth.EmpleadoRepository;
import com.example.web.repository.auth.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

  public static final String SESSION_KEY = "SESSION_USER";

  private final UsuarioRepository usuarioRepo;
  private final ClienteRepository clienteRepo;
  private final EmpleadoRepository empleadoRepo;

  public AuthService(UsuarioRepository usuarioRepo,
                     ClienteRepository clienteRepo,
                     EmpleadoRepository empleadoRepo) {
    this.usuarioRepo = usuarioRepo;
    this.clienteRepo = clienteRepo;
    this.empleadoRepo = empleadoRepo;
  }

  // ====== REGISTER (usuario + cliente con datos completos) ======
  @Transactional
  public RegisterResponse registerCliente(RegisterRequest r) {
    if (r.username() == null || r.username().isBlank())
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username requerido");
    if (usuarioRepo.findByUsernameIgnoreCase(r.username()).isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username ya registrado");

    // Usuario (SIN hash por ahora)
    Usuario u = new Usuario();
    u.setUsername(r.username());
    u.setPassword(r.password() == null ? "" : r.password());
    u.setEstado(true);
    usuarioRepo.save(u);

    // Cliente con datos personales
    Cliente c = new Cliente();
    c.setIdUsuario(u.getId());
    c.setNom(r.nombres());
    c.setApat(r.apat());
    c.setAmat(r.amat());
    c.setDni(r.dni());
    c.setCel(r.cel());
    c.setEmail(r.email());
    c.setFen(r.fen());
    c.setEstado(true);
    clienteRepo.save(c);

    return new RegisterResponse("Registro exitoso", r.username(), "CLIENTE");
  }

  // ====== LOGIN ======
  @Transactional(readOnly = true)
  public LoginResponse login(LoginRequest req, HttpSession session) {
    Usuario user = usuarioRepo.findByUsernameIgnoreCase(req.username())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

    // SIN hash por ahora
    if (!user.getPassword().equals(req.password()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");

    if (!Boolean.TRUE.equals(user.getEstado()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario deshabilitado");

    Long uid = user.getId();
    List<String> roles = new ArrayList<>();
    if (empleadoRepo.existsByIdUsuario(uid)) roles.add("EMPLEADO");
    if (clienteRepo.existsByIdUsuario(uid))  roles.add("CLIENTE");
    if (roles.isEmpty())
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario sin rol");

    SessionUser su = new SessionUser(user.getId(), user.getUsername(), roles, true);
    session.setAttribute(SESSION_KEY, su);

    return new LoginResponse("Login exitoso", roles);
  }

  // ====== LOGOUT ======
  public void logout(HttpSession session) {
    session.invalidate();
  }

  // ====== ME ======
  public SessionUser me(HttpSession session) {
    Object o = session.getAttribute(SESSION_KEY);
    if (o == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sin sesión");
    return (SessionUser) o;
  }
}