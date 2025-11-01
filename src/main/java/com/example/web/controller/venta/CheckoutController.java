package com.example.web.controller.venta;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.dto.venta.CheckoutRequest;
import com.example.web.dto.venta.CheckoutResponse;
import com.example.web.service.auth.GuardService;
import com.example.web.service.venta.CheckoutService;

@RestController
@RequestMapping("/venta/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final GuardService guard;

    public CheckoutController(CheckoutService checkoutService, GuardService guard) {
        this.checkoutService = checkoutService;
        this.guard = guard;
    }

    @PostMapping
    public ResponseEntity<?> realizarCheckout(
            @RequestBody CheckoutRequest request,
            HttpSession session) {

        try {
            // 1) Validar sesión y obtener SessionUser
            var su = guard.requireCliente(session);

            // 2) Pasar idUsuario al servicio (NO email)
            CheckoutResponse response =
                checkoutService.realizarCheckout(request, su.getIdUsuario());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (ResponseStatusException e) { // lanzada por el guard
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
