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
import com.example.web.models.venta.Comprobante;
import com.example.web.models.venta.Venta;
import com.example.web.models.venta.VentaItem;
import com.example.web.repository.auth.ClienteRepository;
import com.example.web.repository.producto.ProductoRepository;
import com.example.web.repository.venta.ComprobanteRepository;
import com.example.web.repository.venta.VentaRepository;

import jakarta.transaction.Transactional;

@Service
public class CheckoutService {

    private final VentaRepository ventaRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    private static final BigDecimal IGV_RATE = new BigDecimal("0.18");

    public CheckoutService(
            VentaRepository ventaRepository,
            ComprobanteRepository comprobanteRepository,
            ProductoRepository productoRepository,
            ClienteRepository clienteRepository) {
        this.ventaRepository = ventaRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public CheckoutResponse realizarCheckout(CheckoutRequest request, Long idUsuario) {

        // 1) Buscar Cliente por idUsuario (no por email)
        Cliente cliente = clienteRepository.findByIdUsuario(idUsuario)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        // 3) Crear Venta
        Venta nuevaVenta = new Venta();
        nuevaVenta.setCliente(cliente);
        nuevaVenta.setFechaVenta(LocalDateTime.now());
        nuevaVenta.setDireccion(request.getDireccion());
        nuevaVenta.setCiudad(request.getCiudad());
        nuevaVenta.setPais(request.getPais());
        nuevaVenta.setCodigoPostal(request.getCodigoPostal());

        BigDecimal totalVenta = BigDecimal.ZERO;
        List<VentaItem> itemsParaGuardar = new ArrayList<>();

        // 4) Validar stock, crear items y acumular totales
        for (VentaItemRequest itemReq : request.getItems()) {
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

        // 5) Comprobante
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
        // Opcional pero claro: setear ambos lados
        ventaGuardada.setComprobante(comprobante);

        // 6) Respuesta
        return new CheckoutResponse(
            ventaGuardada.getId(),
            comprobante.getSerie(),
            comprobante.getNumero(),
            comprobante.getFechaEmision(),
            comprobante.getTotal()
        );
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
            cli.getNom() + " " + cli.getApat() + " " + cli.getAmat()
        );
    }
}
