package com.example.web.controller.venta;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.dto.venta.CheckoutRequest;
import com.example.web.dto.venta.CheckoutResponse;
import com.example.web.service.auth.GuardService;
import com.example.web.service.venta.CheckoutService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpSession;

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
            guard.requireCliente(session);
            String clienteEmail = (String) session.getAttribute("email");

            if (clienteEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se pudo encontrar el email en la sesión.");
            }
            CheckoutResponse response = checkoutService.realizarCheckout(request, clienteEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (SecurityException e) { 
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
