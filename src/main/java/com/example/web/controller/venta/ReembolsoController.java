package com.example.web.controller.venta;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.dto.auth.MeResponse;
import com.example.web.dto.reembolso.ProcesarReembolsoRequest;
import com.example.web.dto.reembolso.SolicitudReembolsoRequest;
import com.example.web.dto.reembolso.SolicitudReembolsoResponse;
import com.example.web.models.auth.Empleado;
import com.example.web.service.auth.GuardService;
import com.example.web.service.venta.ReembolsoService;

@RestController
@RequestMapping("/api/v1/reembolsos")
public class ReembolsoController {

    private final ReembolsoService reembolsoService;
    private final GuardService guard;

    public ReembolsoController(ReembolsoService reembolsoService, GuardService guard) {
        this.reembolsoService = reembolsoService;
        this.guard = guard;
    }

    /**
     * Endpoint para que un cliente solicite un reembolso
     */
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarReembolso(
            @RequestBody SolicitudReembolsoRequest request,
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        try {
            MeResponse cliente = guard.requireCliente(authHeader);

            SolicitudReembolsoResponse response = reembolsoService.solicitarReembolso(request, cliente.idUsuario());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Endpoint para que un empleado/admin procese una solicitud de reembolso
     */
    @PutMapping("/{id}/procesar")
    public ResponseEntity<?> procesarReembolso(
            @PathVariable Long id,
            @RequestBody ProcesarReembolsoRequest request,
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        try {
            Empleado empleado = guard.requireEmpleado(authHeader);

            SolicitudReembolsoResponse response = reembolsoService.procesarReembolso(id, request, empleado);

            return ResponseEntity.ok(response);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Endpoint para que un cliente obtenga sus solicitudes de reembolso
     */
    @GetMapping("/mis-solicitudes")
    public ResponseEntity<?> obtenerMisSolicitudes(
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        try {
            MeResponse cliente = guard.requireCliente(authHeader);

            List<SolicitudReembolsoResponse> solicitudes = reembolsoService.obtenerMisSolicitudes(cliente.idUsuario());

            return ResponseEntity.ok(solicitudes);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Endpoint para que un empleado/admin obtenga las solicitudes pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<?> obtenerSolicitudesPendientes(
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        try {
            guard.requireEmpleado(authHeader);

            List<SolicitudReembolsoResponse> solicitudes = reembolsoService.obtenerSolicitudesPendientes();

            return ResponseEntity.ok(solicitudes);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Endpoint para que un empleado/admin obtenga todas las solicitudes
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodasLasSolicitudes(
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        try {
            guard.requireEmpleado(authHeader);

            List<SolicitudReembolsoResponse> solicitudes = reembolsoService.obtenerTodasLasSolicitudes();

            return ResponseEntity.ok(solicitudes);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }
}
