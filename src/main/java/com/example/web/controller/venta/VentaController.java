package com.example.web.controller.venta;

import com.example.web.dto.venta.ComprobanteDTO;
import com.example.web.service.auth.GuardService;
import com.example.web.service.venta.CheckoutService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    private final CheckoutService checkoutService;
    private final GuardService guard;

    public VentaController(CheckoutService checkoutService, GuardService guard) {
        this.checkoutService = checkoutService;
        this.guard = guard;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVentaById(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        try {
            // 1) Debe ser CLIENTE
            var su = guard.requireCliente(authHeader);

            // 2) Validar pertenencia usando idUsuario
            ComprobanteDTO dto = checkoutService.getVentaComoCliente(id, su.idUsuario());
            return ResponseEntity.ok(dto);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado");
        }
    }
}
