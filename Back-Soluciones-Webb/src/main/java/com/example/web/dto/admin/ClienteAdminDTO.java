package com.example.web.dto.admin;
import com.example.web.models.auth.Cliente;
import com.example.web.models.auth.Usuario;
import java.time.LocalDate;
public record ClienteAdminDTO(
  Long idCliente,
  Long idUsuario,
  String username,
  String password, 
  Boolean usuarioEstado,

  String nom,
  String apat,
  String amat,
  String dni,
  String cel,
  String email,
  LocalDate fen,
  Boolean clienteEstado
) {
  public static ClienteAdminDTO of(Cliente c, Usuario u) {
    return new ClienteAdminDTO(
      c.getId(),
      c.getIdUsuario(),
      u != null ? u.getUsername() : null,
      u != null ? u.getPassword() : null,
      u != null ? u.getEstado()   : null,
      c.getNom(), c.getApat(), c.getAmat(),
      c.getDni(), c.getCel(), c.getEmail(),
      c.getFen(), c.getEstado()
    );
  }
}