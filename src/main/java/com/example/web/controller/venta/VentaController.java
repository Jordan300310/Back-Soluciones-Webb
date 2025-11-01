package com.example.web.controller.venta;

import com.example.web.dto.venta.ComprobanteDTO;
import com.example.web.models.auth.Cliente;
import com.example.web.models.auth.SessionUser;
import com.example.web.repository.auth.ClienteRepository;
import com.example.web.service.auth.GuardService;
import com.example.web.service.venta.CheckoutService; // Reutilizamos el servicio
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/ventas") // Nueva URL base
public class VentaController {

    private final CheckoutService checkoutService;
    private final GuardService guard;
    private final ClienteRepository clienteRepository;

    public VentaController(CheckoutService checkoutService, GuardService guard, ClienteRepository clienteRepository) {
        this.checkoutService = checkoutService;
        this.guard = guard;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVentaById(
            @PathVariable Long id,
            HttpSession session) {
        
        try {
            SessionUser user = guard.requireCliente(session);
            Long idUsuario = user.getIdUsuario();
            Cliente cliente = clienteRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cliente no válido"));

            String clienteEmail = cliente.getEmail();
            ComprobanteDTO dto = checkoutService.getVentaComoCliente(id, clienteEmail);
            return ResponseEntity.ok(dto);
            
        } catch (ResponseStatusException e) {
            
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado");
        }
    }
}