package com.example.web.service.auth;

import com.example.web.dto.auth.LoginRequest;
import com.example.web.dto.auth.LoginResponse;
import com.example.web.dto.auth.MeResponse;
import com.example.web.dto.auth.RegisterRequest;
import com.example.web.dto.auth.RegisterResponse;
import com.example.web.models.auth.Cliente;
import com.example.web.models.auth.Usuario;
import com.example.web.repository.auth.ClienteRepository;
import com.example.web.repository.auth.EmpleadoRepository;
import com.example.web.repository.auth.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

  private final UsuarioRepository usuarioRepo;
  private final ClienteRepository clienteRepo;
  private final EmpleadoRepository empleadoRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(UsuarioRepository usuarioRepo,
                     ClienteRepository clienteRepo,
                     EmpleadoRepository empleadoRepo,
                     PasswordEncoder passwordEncoder,
                     JwtService jwtService) {
    this.usuarioRepo = usuarioRepo;
    this.clienteRepo = clienteRepo;
    this.empleadoRepo = empleadoRepo;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  // ====== REGISTER (CLIENTE) ======
  @Transactional
  public RegisterResponse registerCliente(RegisterRequest r) {
    if (r.username() == null || r.username().isBlank())
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username requerido");
    if (usuarioRepo.findByUsernameIgnoreCase(r.username()).isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username ya registrado");
    if (r.password() == null || r.password().isBlank())
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password requerida");

    Usuario u = new Usuario();
    u.setUsername(r.username());
    u.setPassword(passwordEncoder.encode(r.password()));  // HASH
    u.setEstado(true);
    usuarioRepo.save(u);

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
  public LoginResponse login(LoginRequest req) {
    Usuario user = usuarioRepo.findByUsernameIgnoreCase(req.username())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

    if (!passwordEncoder.matches(req.password(), user.getPassword()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");

    if (!Boolean.TRUE.equals(user.getEstado()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario deshabilitado");

    Long uid = user.getId();
    List<String> roles = new ArrayList<>();
    if (empleadoRepo.existsByIdUsuario(uid)) roles.add("EMPLEADO");
    if (clienteRepo.existsByIdUsuario(uid))  roles.add("CLIENTE");
    if (roles.isEmpty())
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario sin rol");

    boolean enabled = true;

    String token = jwtService.generateToken(uid, user.getUsername(), roles, enabled);

    return new LoginResponse("Login exitoso", roles, token);
  }

  // ====== ME (a partir de Authorization: Bearer xxx) ======
  public MeResponse me(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer "))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");

    String token = authHeader.substring(7);
    return jwtService.parseToken(token);
  }
}
