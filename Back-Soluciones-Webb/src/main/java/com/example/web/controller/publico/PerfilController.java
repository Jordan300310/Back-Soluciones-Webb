package com.example.web.controller.publico;

import com.example.web.dto.perfil.ChangePasswordRequest;
import com.example.web.dto.perfil.PedidoResponse;
import com.example.web.dto.perfil.PerfilResponse;
import com.example.web.dto.perfil.PerfilUpdateRequest;
import com.example.web.service.auth.GuardService;
import com.example.web.service.perfil.PerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController
@RequestMapping("/perfil")
public class PerfilController {

  private final PerfilService perfilService;
  private final GuardService guard;

  public PerfilController(PerfilService perfilService,
                          GuardService guard) {
    this.perfilService = perfilService;
    this.guard = guard;
  }

  @GetMapping
  public ResponseEntity<?> getPerfil(
      @RequestHeader(name = "Authorization", required = false) String authHeader) {

    try {
      var me = guard.requireCliente(authHeader);
      Long idUsuario = me.idUsuario();

      PerfilResponse response = perfilService.getPerfil(idUsuario);
      return ResponseEntity.ok(response);

    } catch (ResponseStatusException e) {
      return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping
  public ResponseEntity<?> updatePerfil(
      @RequestHeader(name = "Authorization", required = false) String authHeader,
      @RequestBody PerfilUpdateRequest request) {

    try {
      var me = guard.requireCliente(authHeader);
      Long idUsuario = me.idUsuario();

      PerfilResponse response = perfilService.actualizarPerfil(idUsuario, request);
      return ResponseEntity.ok(response);

    } catch (ResponseStatusException e) {
      return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/cambiar-password")
  public ResponseEntity<?> changePassword(
      @RequestHeader(name = "Authorization", required = false) String authHeader,
      @RequestBody ChangePasswordRequest request) {

    try {
      var me = guard.requireCliente(authHeader);
      Long idUsuario = me.idUsuario();

      perfilService.cambiarPassword(idUsuario, request);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    } catch (ResponseStatusException e) {
      return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
   
  @GetMapping("/pedidos")
  public List<PedidoResponse> listarPedidos(
      @RequestHeader(name = "Authorization", required = false) String authHeader) {

    var me = guard.requireCliente(authHeader);
    return perfilService.listarPedidos(me.idUsuario());
  }
}
