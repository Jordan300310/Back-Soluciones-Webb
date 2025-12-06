package com.example.web.service.perfil;

import com.example.web.dto.perfil.ChangePasswordRequest;
import com.example.web.dto.perfil.PedidoResponse;

import com.example.web.dto.perfil.PerfilResponse;
import com.example.web.dto.perfil.PerfilUpdateRequest;
import com.example.web.models.auth.Cliente;
import com.example.web.models.auth.Usuario;
import com.example.web.models.venta.Venta;
import com.example.web.repository.auth.ClienteRepository;
import com.example.web.repository.auth.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import com.example.web.repository.venta.VentaRepository;
@Service
@Transactional
public class PerfilService {

  private final UsuarioRepository usuarioRepo;
  private final ClienteRepository clienteRepo;
  private final VentaRepository ventaRepo;
  private final PasswordEncoder passwordEncoder;

  public PerfilService(UsuarioRepository usuarioRepo,
                       ClienteRepository clienteRepo,
                       VentaRepository ventaRepo,
                       PasswordEncoder passwordEncoder) {
    this.usuarioRepo = usuarioRepo;
    this.clienteRepo = clienteRepo;
    this.ventaRepo = ventaRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional(readOnly = true)
  public PerfilResponse getPerfil(Long idUsuario) {
    Usuario usuario = usuarioRepo.findById(idUsuario)
        .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    Cliente cliente = clienteRepo.findByIdUsuario(idUsuario)
        .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado para este usuario"));

    return PerfilResponse.from(usuario, cliente);
  }

  public PerfilResponse actualizarPerfil(Long idUsuario, PerfilUpdateRequest request) {
    Usuario usuario = usuarioRepo.findById(idUsuario)
        .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    Cliente cliente = clienteRepo.findByIdUsuario(idUsuario)
        .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado para este usuario"));

    request.applyTo(cliente);
    clienteRepo.save(cliente);

    return PerfilResponse.from(usuario, cliente);
  }

  public void cambiarPassword(Long idUsuario, ChangePasswordRequest request) {
    if (request.currentPassword() == null || request.currentPassword().isBlank())
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña actual requerida");
    if (request.newPassword() == null || request.newPassword().isBlank())
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nueva contraseña requerida");

    Usuario usuario = usuarioRepo.findById(idUsuario)
        .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    if (!passwordEncoder.matches(request.currentPassword(), usuario.getPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual no es correcta");
    }

    usuario.setPassword(passwordEncoder.encode(request.newPassword()));
    usuarioRepo.save(usuario);
  }
     public List<PedidoResponse> listarPedidos(Long idUsuario) {
    Cliente c = clienteRepo.findByIdUsuario(idUsuario)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

    List<Venta> ventas =
        ventaRepo.findByClienteIdOrderByFechaVentaDesc(c.getId());

    return ventas.stream()
        .map(PedidoResponse::from)
        .toList();
  }
}