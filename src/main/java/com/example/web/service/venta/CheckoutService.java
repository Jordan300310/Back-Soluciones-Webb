package com.example.web.service.venta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.dto.venta.CheckoutRequest;
import com.example.web.dto.venta.CheckoutResponse;
import com.example.web.dto.venta.ComprobanteDTO;
import com.example.web.dto.venta.VentaItemRequest;
import com.example.web.models.Producto.Producto;
import com.example.web.models.auth.Cliente;
import com.example.web.models.venta.CheckoutPendiente;
import com.example.web.models.venta.Comprobante;
import com.example.web.models.venta.Venta;
import com.example.web.models.venta.VentaItem;
import com.example.web.repository.auth.ClienteRepository;
import com.example.web.repository.producto.ProductoRepository;
import com.example.web.repository.venta.CheckoutPendienteRepository;
import com.example.web.repository.venta.ComprobanteRepository;
import com.example.web.repository.venta.VentaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import jakarta.transaction.Transactional;

@Service
public class CheckoutService {

    private final VentaRepository ventaRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final CheckoutPendienteRepository checkoutPendienteRepository;
    private final MercadoPagoService mercadoPagoService;
    private final ObjectMapper objectMapper;

    private static final BigDecimal IGV_RATE = new BigDecimal("0.18");

    public CheckoutService(
            VentaRepository ventaRepository,
            ComprobanteRepository comprobanteRepository,
            ProductoRepository productoRepository,
            ClienteRepository clienteRepository,
            CheckoutPendienteRepository checkoutPendienteRepository,
            MercadoPagoService mercadoPagoService,
            ObjectMapper objectMapper) {
        this.ventaRepository = ventaRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.checkoutPendienteRepository = checkoutPendienteRepository;
        this.mercadoPagoService = mercadoPagoService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public CheckoutResponse realizarCheckout(CheckoutRequest request, Long idUsuario) {
        try {
            Cliente cliente = clienteRepository.findByIdUsuario(idUsuario)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            BigDecimal totalVenta = BigDecimal.ZERO;
            List<Producto> productos = new ArrayList<>();

            for (VentaItemRequest itemReq : request.getItems()) {
                Producto producto = productoRepository.findById(itemReq.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemReq.getProductoId()));

                if (producto.getStock() < itemReq.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
                }

                productos.add(producto);
                BigDecimal subtotalItem = producto.getPrecio().multiply(BigDecimal.valueOf(itemReq.getCantidad()));
                totalVenta = totalVenta.add(subtotalItem);
            }

            CheckoutPendiente checkoutPendiente = new CheckoutPendiente();
            checkoutPendiente.setCliente(cliente);
            checkoutPendiente.setDireccion(request.getDireccion());
            checkoutPendiente.setCiudad(request.getCiudad());
            checkoutPendiente.setPais(request.getPais());
            checkoutPendiente.setCodigoPostal(request.getCodigoPostal());
            checkoutPendiente.setTotal(totalVenta);
            checkoutPendiente.setEstado("PENDING");
            checkoutPendiente.setFechaCreacion(LocalDateTime.now());
            checkoutPendiente.setFechaExpiracion(LocalDateTime.now().plusHours(24));

            try {
                String itemsJson = objectMapper.writeValueAsString(request.getItems());
                checkoutPendiente.setItemsJson(itemsJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error al serializar items", e);
            }

            CheckoutPendiente checkoutGuardado = checkoutPendienteRepository.save(checkoutPendiente);

            String externalReference = "CHECKOUT-" + checkoutGuardado.getId();
            Preference preference = mercadoPagoService.crearPreferencia(
                    request.getItems(),
                    productos,
                    externalReference);

            checkoutGuardado.setPreferenceId(preference.getId());
            checkoutPendienteRepository.save(checkoutGuardado);

            return new CheckoutResponse(
                    preference.getInitPoint(),
                    preference.getId(),
                    totalVenta,
                    checkoutGuardado.getId());

        } catch (MPException | MPApiException e) {
            throw new RuntimeException("Error al crear preferencia de pago: " + e.getMessage(), e);
        }
    }

    /**
     * Confirma una venta después de recibir confirmación de pago de Mercado Pago
     */
    @Transactional
    public Venta confirmarVenta(String preferenceId, String paymentId, String paymentStatus, String paymentMethod) {

        CheckoutPendiente checkoutPendiente = checkoutPendienteRepository.findByPreferenceId(preferenceId)
                .orElseThrow(() -> new RuntimeException("Checkout pendiente no encontrado"));

        if ("COMPLETED".equals(checkoutPendiente.getEstado())) {
            throw new RuntimeException("Este checkout ya fue procesado");
        }

        List<VentaItemRequest> items;
        try {
            items = objectMapper.readValue(
                    checkoutPendiente.getItemsJson(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, VentaItemRequest.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al deserializar items", e);
        }

        Venta nuevaVenta = new Venta();
        nuevaVenta.setCliente(checkoutPendiente.getCliente());
        nuevaVenta.setFechaVenta(LocalDateTime.now());
        nuevaVenta.setDireccion(checkoutPendiente.getDireccion());
        nuevaVenta.setCiudad(checkoutPendiente.getCiudad());
        nuevaVenta.setPais(checkoutPendiente.getPais());
        nuevaVenta.setCodigoPostal(checkoutPendiente.getCodigoPostal());
        nuevaVenta.setMercadoPagoPreferenceId(preferenceId);
        nuevaVenta.setMercadoPagoPaymentId(paymentId);
        nuevaVenta.setEstadoPago(paymentStatus);
        nuevaVenta.setMetodoPago(paymentMethod);

        BigDecimal totalVenta = BigDecimal.ZERO;
        List<VentaItem> itemsParaGuardar = new ArrayList<>();

        for (VentaItemRequest itemReq : items) {
            Producto producto = productoRepository.findById(itemReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemReq.getProductoId()));

            if (producto.getStock() < itemReq.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - itemReq.getCantidad());
            productoRepository.save(producto);

            VentaItem ventaItem = new VentaItem();
            ventaItem.setVenta(nuevaVenta);
            ventaItem.setProducto(producto);
            ventaItem.setCantidad(itemReq.getCantidad());
            ventaItem.setPrecioUnitario(producto.getPrecio());
            itemsParaGuardar.add(ventaItem);

            BigDecimal subtotalItem = producto.getPrecio().multiply(BigDecimal.valueOf(itemReq.getCantidad()));
            totalVenta = totalVenta.add(subtotalItem);
        }

        nuevaVenta.setTotal(totalVenta);
        nuevaVenta.setItems(itemsParaGuardar);
        Venta ventaGuardada = ventaRepository.save(nuevaVenta);

        Comprobante comprobante = new Comprobante();
        comprobante.setVenta(ventaGuardada);
        comprobante.setFechaEmision(LocalDateTime.now());
        comprobante.setMoneda("PEN");
        comprobante.setRucEmisor("10203040501");
        comprobante.setSerie("B001");
        comprobante.setNumero(String.format("%08d", ventaGuardada.getId()));

        BigDecimal subtotal = totalVenta.divide(IGV_RATE.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
        BigDecimal igv = totalVenta.subtract(subtotal);

        comprobante.setSubtotal(subtotal);
        comprobante.setIgv(igv);
        comprobante.setTotal(totalVenta);

        comprobanteRepository.save(comprobante);
        ventaGuardada.setComprobante(comprobante);

        checkoutPendiente.setEstado("COMPLETED");
        checkoutPendienteRepository.save(checkoutPendiente);

        return ventaGuardada;
    }

    /**
     * Confirma una venta usando el external reference de Mercado Pago
     * El external reference tiene el formato "CHECKOUT-{checkoutPendienteId}"
     */
    @Transactional
    public Venta confirmarVentaPorExternalReference(String externalReference, String paymentId, String paymentStatus,
            String paymentMethod) {

        if (externalReference == null || !externalReference.startsWith("CHECKOUT-")) {
            throw new RuntimeException("External reference inválido: " + externalReference);
        }

        String checkoutIdStr = externalReference.substring("CHECKOUT-".length());
        Long checkoutPendienteId;
        try {
            checkoutPendienteId = Long.parseLong(checkoutIdStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("ID de checkout inválido en external reference: " + externalReference, e);
        }

        CheckoutPendiente checkoutPendiente = checkoutPendienteRepository.findById(checkoutPendienteId)
                .orElseThrow(
                        () -> new RuntimeException("Checkout pendiente no encontrado con ID: " + checkoutPendienteId));

        return confirmarVenta(checkoutPendiente.getPreferenceId(), paymentId, paymentStatus, paymentMethod);
    }

    @Transactional
    public ComprobanteDTO getVentaComoCliente(Long ventaId, Long idUsuario) {

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        if (!venta.getCliente().getIdUsuario().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado a esta venta");
        }

        Comprobante c = venta.getComprobante();
        Cliente cli = venta.getCliente();

        return new ComprobanteDTO(
                venta.getId(),
                c.getSerie(),
                c.getNumero(),
                c.getFechaEmision(),
                c.getSubtotal(),
                c.getIgv(),
                c.getTotal(),
                cli.getNom() + " " + cli.getApat() + " " + cli.getAmat());
    }
}
