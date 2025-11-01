package com.example.web.controller.venta;

import com.example.web.dto.venta.ComprobanteDTO;
import com.example.web.service.auth.GuardService;
import com.example.web.service.venta.CheckoutService;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<?> getVentaById(@PathVariable Long id, HttpSession session) {
        try {
            // 1) Debe ser CLIENTE y obtener su SessionUser
            var su = guard.requireCliente(session);


            // 2) Pasar idUsuario al servicio (el servicio valida pertenencia)
            ComprobanteDTO dto = checkoutService.getVentaComoCliente(id, su.getIdUsuario());
            return ResponseEntity.ok(dto);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado");
        }
    }
}
