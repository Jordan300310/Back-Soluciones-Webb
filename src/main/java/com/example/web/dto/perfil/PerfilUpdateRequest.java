package com.example.web.dto.perfil;


import com.example.web.models.auth.Cliente;

public record PerfilUpdateRequest(
    String nombres,
    String apellidoPaterno,
    String apellidoMaterno,
    String celular,
    String email
) {
  public void applyTo(Cliente c) {
    if (c == null) return;
    if (nombres != null)         c.setNom(nombres);
    if (apellidoPaterno != null) c.setApat(apellidoPaterno);
    if (apellidoMaterno != null) c.setAmat(apellidoMaterno);
    if (celular != null)         c.setCel(celular);
    if (email != null)           c.setEmail(email);
  }
}