package com.example.web.controller.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.web.dto.admin.ProductoAdminDTO;
import com.example.web.models.Producto.Producto;
import com.example.web.service.auth.GuardService;
import com.example.web.service.admin.AdminProductoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/productos")
public class AdminProductoController {

  private final AdminProductoService service;
  private final GuardService guard;

  public AdminProductoController(AdminProductoService service, GuardService guard) {
    this.service = service;
    this.guard = guard;
  }

  @PostMapping
  public ResponseEntity<ProductoAdminDTO> create(@RequestBody Producto body, HttpSession session) {
    guard.requireAdmin(session);
    var p = service.create(body);
    return ResponseEntity.created(URI.create("/admin/productos/" + p.getId())).body(ProductoAdminDTO.of(p));
  }

  @GetMapping
  public List<ProductoAdminDTO> list(HttpSession session) {
    guard.requireEmpleado(session);
    return service.list().stream().map(ProductoAdminDTO::of).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductoAdminDTO> get(@PathVariable Long id, HttpSession session) {
    guard.requireEmpleado(session);
    var p = service.get(id);
    return (p == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(ProductoAdminDTO.of(p));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ProductoAdminDTO> update(@PathVariable Long id,
                                                 @RequestBody Producto body,
                                                 HttpSession session) {
    guard.requireAdmin(session);
    var p = service.update(id, body);
    return (p == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(ProductoAdminDTO.of(p));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session) {
    guard.requireAdmin(session);
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
