package com.example.web.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.web.dto.admin.CrearEmpleadoRequest;
import com.example.web.dto.admin.EmpleadoAdminDTO;
import com.example.web.dto.admin.UpdateEmpleadoRequest;
import com.example.web.service.admin.AdminEmpleadoService;
import com.example.web.service.auth.GuardService;
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
  public ResponseEntity<EmpleadoAdminDTO> crear(@RequestBody CrearEmpleadoRequest body,@RequestHeader(name = "Authorization", required = false) String authHeader) {
    guard.requireAdmin(authHeader); // solo admin
    var dto = service.crear(body);
    return ResponseEntity.created(URI.create("/admin/empleados/" + dto.idEmpleado())).body(dto);
  }

  @GetMapping
  public List<EmpleadoAdminDTO> list(@RequestHeader(name = "Authorization", required = false) String authHeader) {
    guard.requireEmpleado(authHeader);
    return service.list();
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmpleadoAdminDTO> get(@PathVariable Long id, @RequestHeader(name = "Authorization", required = false) String authHeader) {
    guard.requireEmpleado(authHeader);
    var dto = service.get(id);
    return (dto == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<EmpleadoAdminDTO> update(@PathVariable Long id,
                                                 @RequestBody UpdateEmpleadoRequest body,
                                                 @RequestHeader(name = "Authorization", required = false) String authHeader) {
    guard.requireAdmin(authHeader);
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
