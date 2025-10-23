package com.example.web.controller.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.web.models.Proveedor.Proveedor;
import com.example.web.service.auth.GuardService;
import com.example.web.service.admin.AdminProveedorService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/proveedores")
public class AdminProveedorController {
  private final AdminProveedorService service;
  private final GuardService guard;

  public AdminProveedorController(AdminProveedorService service, GuardService guard) {
    this.service = service;
    this.guard = guard;
  }

  @PostMapping
  public ResponseEntity<Proveedor> create(@RequestBody Proveedor body, HttpSession session) {
    guard.requireAdmin(session);
    var p = service.create(body);
    return ResponseEntity.created(URI.create("/admin/proveedores/" + p.getId())).body(p);
  }

  @GetMapping
  public List<Proveedor> list(HttpSession session) {
    guard.requireEmpleado(session); // empleado o admin
    return service.list();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Proveedor> get(@PathVariable Long id, HttpSession session) {
    guard.requireEmpleado(session);
    var p = service.get(id);
    return (p == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Proveedor> update(@PathVariable Long id,
                                          @RequestBody Proveedor body,
                                          HttpSession session) {
    guard.requireAdmin(session);
    var p = service.update(id, body);
    return (p == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session) {
    guard.requireAdmin(session);
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
