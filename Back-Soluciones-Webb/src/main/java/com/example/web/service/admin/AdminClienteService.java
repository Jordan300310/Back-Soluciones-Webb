package com.example.web.service.admin;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.dto.admin.ClienteAdminDTO;
import com.example.web.dto.admin.UpdateClienteRequest;
import com.example.web.models.auth.Cliente;
import com.example.web.models.auth.Usuario;
import com.example.web.repository.auth.ClienteRepository;
import com.example.web.repository.auth.UsuarioRepository;
import java.util.Objects;
import java.util.List;

@Service
public class AdminClienteService {

  private final ClienteRepository clienteRepo;
  private final UsuarioRepository usuarioRepo;

  public AdminClienteService(ClienteRepository clienteRepo, UsuarioRepository usuarioRepo) {
    this.clienteRepo = clienteRepo;
    this.usuarioRepo = usuarioRepo;
  }

  @Transactional(readOnly = true)
public List<ClienteAdminDTO> list() {
  return clienteRepo.findByEstadoTrue().stream()     
      .map(c -> {
        if (c.getIdUsuario() == null) {
          return null;
        }

        Usuario u = usuarioRepo
            .findByIdAndEstadoTrue(c.getIdUsuario())   
            .orElse(null);

        if (u == null) {
          return null; 
        }

        return ClienteAdminDTO.of(c, u);
      })
      .filter(Objects::nonNull)                   
      .toList();
}

  @Transactional(readOnly = true)
  public ClienteAdminDTO get(Long id) {
    Cliente c = clienteRepo.findById(id).orElse(null);
    if (c == null) return null;
    Usuario u = (c.getIdUsuario() != null)
        ? usuarioRepo.findById(c.getIdUsuario()).orElse(null)
        : null;
    return ClienteAdminDTO.of(c, u);
  }

  @Transactional
  public ClienteAdminDTO update(Long id, UpdateClienteRequest in) {
    Cliente c = clienteRepo.findById(id).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

    if (in.nom()  != null) c.setNom(in.nom());
    if (in.apat() != null) c.setApat(in.apat());
    if (in.amat() != null) c.setAmat(in.amat());
    if (in.dni()  != null) c.setDni(in.dni());
    if (in.cel()  != null) c.setCel(in.cel());
    if (in.email()!= null) c.setEmail(in.email());
    if (in.fen()  != null) c.setFen(in.fen());
    clienteRepo.save(c);


    Usuario u = null;
    if (c.getIdUsuario() != null) {
      u = usuarioRepo.findById(c.getIdUsuario()).orElse(null);
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

    return ClienteAdminDTO.of(c, u);
  }

  @Transactional
  public void delete(Long id) {
    Cliente c = clienteRepo.findById(id).orElse(null);
    if (c != null) {

      c.setEstado(false);
      clienteRepo.save(c);

      Long idUsuario = c.getIdUsuario();
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
