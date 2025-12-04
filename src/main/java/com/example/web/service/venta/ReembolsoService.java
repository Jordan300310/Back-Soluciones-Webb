package com.example.web.service.venta;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.dto.reembolso.ProcesarReembolsoRequest;
import com.example.web.dto.reembolso.SolicitudReembolsoRequest;
import com.example.web.dto.reembolso.SolicitudReembolsoResponse;
import com.example.web.models.auth.Cliente;
import com.example.web.models.auth.Empleado;
import com.example.web.models.venta.SolicitudReembolso;
import com.example.web.models.venta.Venta;
import com.example.web.models.venta.VentaItem;
import com.example.web.repository.auth.ClienteRepository;
import com.example.web.repository.producto.ProductoRepository;
import com.example.web.repository.venta.SolicitudReembolsoRepository;
import com.example.web.repository.venta.VentaRepository;

@Service
public class ReembolsoService {

    private static final int DIAS_LIMITE_REEMBOLSO = 30;

    private final SolicitudReembolsoRepository solicitudReembolsoRepository;
    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public ReembolsoService(
            SolicitudReembolsoRepository solicitudReembolsoRepository,
            VentaRepository ventaRepository,
            ClienteRepository clienteRepository,
            ProductoRepository productoRepository) {
        this.solicitudReembolsoRepository = solicitudReembolsoRepository;
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Permite a un cliente solicitar un reembolso para una venta
     */
    @Transactional
    public SolicitudReembolsoResponse solicitarReembolso(SolicitudReembolsoRequest request, Long idUsuario) {
        Venta venta = ventaRepository.findById(request.getIdVenta())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        Cliente cliente = clienteRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        if (!venta.getCliente().getId().equals(cliente.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta venta no pertenece al cliente");
        }

        if ("REEMBOLSADO".equalsIgnoreCase(venta.getEstadoPago())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta venta ya fue reembolsada");
        }

        solicitudReembolsoRepository.findSolicitudActivaByVentaId(request.getIdVenta())
                .ifPresent(s -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Ya existe una solicitud de reembolso " + s.getEstado().toLowerCase() + " para esta venta");
                });

        long diasDesdeVenta = ChronoUnit.DAYS.between(venta.getFechaVenta(), LocalDateTime.now());
        if (diasDesdeVenta > DIAS_LIMITE_REEMBOLSO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El plazo para solicitar reembolso ha expirado (máximo " + DIAS_LIMITE_REEMBOLSO + " días)");
        }

        SolicitudReembolso solicitud = new SolicitudReembolso();
        solicitud.setVenta(venta);
        solicitud.setCliente(cliente);
        solicitud.setMotivo(request.getMotivo());
        solicitud.setEstado("PENDIENTE");
        solicitud.setFechaSolicitud(LocalDateTime.now());

        solicitud = solicitudReembolsoRepository.save(solicitud);

        return mapToResponse(solicitud);
    }

    /**
     * Permite a un empleado/admin procesar (aprobar o rechazar) una solicitud de
     * reembolso
     */
    @Transactional
    public SolicitudReembolsoResponse procesarReembolso(Long solicitudId, ProcesarReembolsoRequest request,
            Empleado empleado) {
        SolicitudReembolso solicitud = solicitudReembolsoRepository.findById(solicitudId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Solicitud de reembolso no encontrada"));

        if (!"PENDIENTE".equalsIgnoreCase(solicitud.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Esta solicitud ya fue procesada con estado: " + solicitud.getEstado());
        }

        if (request.getAprobar() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe especificar si aprueba o rechaza el reembolso");
        }

        if (request.getAprobar()) {
            aprobarReembolso(solicitud);
        } else {
            solicitud.setEstado("RECHAZADO");
        }

        solicitud.setFechaProcesamiento(LocalDateTime.now());
        solicitud.setProcesadoPor(empleado);
        solicitud.setComentarioEmpleado(request.getComentario());

        solicitud = solicitudReembolsoRepository.save(solicitud);

        return mapToResponse(solicitud);
    }

    /**
     * Aprueba un reembolso: restaura stock y actualiza estado de venta
     */
    private void aprobarReembolso(SolicitudReembolso solicitud) {
        Venta venta = solicitud.getVenta();

        for (VentaItem item : venta.getItems()) {
            var producto = item.getProducto();
            producto.setStock(producto.getStock() + item.getCantidad());
            productoRepository.save(producto);
        }

        venta.setEstadoPago("REEMBOLSADO");
        ventaRepository.save(venta);

        solicitud.setEstado("APROBADO");
    }

    /**
     * Obtiene todas las solicitudes de reembolso de un cliente
     */
    @Transactional(readOnly = true)
    public List<SolicitudReembolsoResponse> obtenerMisSolicitudes(Long idUsuario) {
        Cliente cliente = clienteRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        List<SolicitudReembolso> solicitudes = solicitudReembolsoRepository
                .findByClienteIdOrderByFechaSolicitudDesc(cliente.getId());

        return solicitudes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las solicitudes pendientes (para empleados/admin)
     */
    @Transactional(readOnly = true)
    public List<SolicitudReembolsoResponse> obtenerSolicitudesPendientes() {
        List<SolicitudReembolso> solicitudes = solicitudReembolsoRepository.findByEstado("PENDIENTE");

        return solicitudes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las solicitudes (para empleados/admin)
     */
    @Transactional(readOnly = true)
    public List<SolicitudReembolsoResponse> obtenerTodasLasSolicitudes() {
        List<SolicitudReembolso> solicitudes = solicitudReembolsoRepository.findAll();

        return solicitudes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Mapea una entidad SolicitudReembolso a un DTO de respuesta
     */
    private SolicitudReembolsoResponse mapToResponse(SolicitudReembolso solicitud) {
        SolicitudReembolsoResponse response = new SolicitudReembolsoResponse();
        response.setId(solicitud.getId());
        response.setIdVenta(solicitud.getVenta().getId());

        String nombreCliente = solicitud.getCliente().getNom();
        if (solicitud.getCliente().getApat() != null) {
            nombreCliente += " " + solicitud.getCliente().getApat();
        }
        if (solicitud.getCliente().getAmat() != null) {
            nombreCliente += " " + solicitud.getCliente().getAmat();
        }
        response.setNombreCliente(nombreCliente);

        response.setEmailCliente(solicitud.getCliente().getEmail());
        response.setTotalVenta(solicitud.getVenta().getTotal());
        response.setMotivo(solicitud.getMotivo());
        response.setEstado(solicitud.getEstado());
        response.setFechaSolicitud(solicitud.getFechaSolicitud());
        response.setFechaProcesamiento(solicitud.getFechaProcesamiento());

        if (solicitud.getProcesadoPor() != null) {
            String nombreEmpleado = solicitud.getProcesadoPor().getNom();
            if (solicitud.getProcesadoPor().getApat() != null) {
                nombreEmpleado += " " + solicitud.getProcesadoPor().getApat();
            }
            if (solicitud.getProcesadoPor().getAmat() != null) {
                nombreEmpleado += " " + solicitud.getProcesadoPor().getAmat();
            }
            response.setNombreEmpleadoProceso(nombreEmpleado);
        }

        response.setComentarioEmpleado(solicitud.getComentarioEmpleado());

        return response;
    }
}
