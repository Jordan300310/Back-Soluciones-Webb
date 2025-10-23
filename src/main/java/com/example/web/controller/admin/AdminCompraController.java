package com.example.web.controller.admin;

import com.example.web.dto.compra.CompraRequest;
import com.example.web.dto.compra.CompraCreatedDTO;
import com.example.web.dto.compra.CompraListDTO;
import com.example.web.service.admin.AdminCompraService;
import com.example.web.service.auth.GuardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/compras")
public class AdminCompraController {

  private final AdminCompraService service;
  private final GuardService guard;

  public AdminCompraController(AdminCompraService service, GuardService guard) {
    this.service = service;
    this.guard = guard;
  }

  @PostMapping
  public ResponseEntity<CompraCreatedDTO> crear(@RequestBody CompraRequest body, HttpSession session) {
    guard.requireEmpleado(session); // empleado o admin
    var dto = service.crear(body);
    return ResponseEntity.created(URI.create("/admin/compras/" + dto.idCompra())).body(dto);
  }

  @GetMapping
  public List<CompraListDTO> listar(HttpSession session) {
    guard.requireEmpleado(session);
    return service.listar();
  }
}