package com.example.web.controller.venta;

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
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @RequestBody CheckoutRequest request) {

        try {
            // 1) Validar JWT y rol CLIENTE
            var su = guard.requireCliente(authHeader);

            // 2) Pasar idUsuario al servicio (NO email)
            CheckoutResponse response =
                checkoutService.realizarCheckout(request, su.idUsuario());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (ResponseStatusException e) { 
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
