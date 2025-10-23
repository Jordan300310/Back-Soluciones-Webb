package com.example.web.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.web.dto.admin.CrearEmpleadoRequest;
import com.example.web.dto.admin.EmpleadoAdminDTO;
import com.example.web.dto.admin.UpdateEmpleadoRequest;
import com.example.web.service.admin.AdminEmpleadoService;
import com.example.web.service.auth.GuardService;
import jakarta.servlet.http.HttpSession;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/empleados")
public class AdminEmpleadoController {

  private final AdminEmpleadoService service;
  private final GuardService guard;

  public AdminEmpleadoController(AdminEmpleadoService service, GuardService guard) {
    this.service = service;
    this.guard = guard;
  }

  @PostMapping
  public ResponseEntity<EmpleadoAdminDTO> crear(@RequestBody CrearEmpleadoRequest body, HttpSession session) {
    guard.requireAdmin(session);
    var dto = service.crear(body);
    return ResponseEntity.created(URI.create("/admin/empleados/" + dto.idEmpleado())).body(dto);
  }

  @GetMapping
  public List<EmpleadoAdminDTO> list(HttpSession session) {
    guard.requireEmpleado(session);
    return service.list();
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmpleadoAdminDTO> get(@PathVariable Long id, HttpSession session) {
    guard.requireEmpleado(session);
    var dto = service.get(id);
    return (dto == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<EmpleadoAdminDTO> update(@PathVariable Long id,
                                                 @RequestBody UpdateEmpleadoRequest body,
                                                 HttpSession session) {
    guard.requireAdmin(session);
    var dto = service.update(id, body);
    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session) {
    guard.requireAdmin(session);
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
