package com.example.web.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.web.dto.common.ComboDTO;
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
  public ResponseEntity<Proveedor> create(@RequestBody Proveedor body, @RequestHeader(name = "Authorization", required = false) String authHeader) {
    guard.requireAdmin(authHeader);
    var p = service.create(body);
    return ResponseEntity.created(URI.create("/admin/proveedores/" + p.getId())).body(p);
  }

  @GetMapping
  public List<Proveedor> list(@RequestHeader(name = "Authorization", required = false) String authHeader) {
    guard.requireEmpleado(authHeader); // empleado o admin
    return service.list();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Proveedor> get(@PathVariable Long id, @RequestHeader(name = "Authorization", required = false) String authHeader) {
    guard.requireEmpleado(authHeader);
    var p = service.get(id);
    return (p == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Proveedor> update(@PathVariable Long id,
                                          @RequestBody Proveedor body,
                                          @RequestHeader(name = "Authorization", required = false) String authHeader) {
    guard.requireAdmin(authHeader);
    var p = service.update(id, body);
    return (p == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
  } 

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader(name = "Authorization", required = false) String authHeader) {
    guard.requireAdmin(authHeader);
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
   @GetMapping("/activos")
    public List<ComboDTO> activos(
            @RequestHeader(name = "Authorization", required = false) String authHeader
    ) {
        guard.requireEmpleado(authHeader);
        return service.listarActivosCombo();
    }
}
