package com.example.web.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.web.dto.admin.ClienteAdminDTO;
import com.example.web.dto.admin.UpdateClienteRequest;
import com.example.web.service.admin.AdminClienteService;
import com.example.web.service.auth.GuardService;

import java.util.List;

@RestController
@RequestMapping("/admin/clientes")
public class AdminClienteController {

  private final AdminClienteService service;
  private final GuardService guard;

  public AdminClienteController(AdminClienteService service, GuardService guard) {
    this.service = service;
    this.guard = guard;
  }

  @GetMapping
  public List<ClienteAdminDTO> list(@RequestHeader(name = "Authorization", required = false)String authHeader) {
    guard.requireEmpleado(authHeader); // empleado o admin
    return service.list();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ClienteAdminDTO> get(@PathVariable Long id, @RequestHeader(name = "Authorization", required = false)String authHeader) {
    guard.requireEmpleado(authHeader);
    var dto = service.get(id);
    return (dto == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ClienteAdminDTO> update(@PathVariable Long id,
                                                @RequestBody UpdateClienteRequest body,
                                                @RequestHeader(name = "Authorization", required = false) String authHeader) {
    guard.requireAdmin(authHeader); // solo admin
    var dto = service.update(id, body);
    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader(name = "Authorization", required = false) String authHeader) {
    guard.requireAdmin(authHeader);
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}