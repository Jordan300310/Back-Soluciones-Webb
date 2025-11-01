package com.example.web.service.venta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired private VentaRepository ventaRepository;
    @Autowired private ComprobanteRepository comprobanteRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ClienteRepository clienteRepository;

    private static final BigDecimal IGV_RATE = new BigDecimal("0.18");

    @Transactional
    public CheckoutResponse realizarCheckout(CheckoutRequest request, String clienteEmail) {

        // 1. Buscar al Cliente
        Cliente cliente = clienteRepository.findByEmail(clienteEmail)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // 2. Actualizar dirección del cliente
        cliente.setDireccion(request.getDireccion());
        cliente.setCiudad(request.getCiudad());
        cliente.setPais(request.getPais());
        cliente.setCodigoPostal(request.getCodigoPostal());
        clienteRepository.save(cliente);

        // 3. Crear la Venta
        Venta nuevaVenta = new Venta();
        nuevaVenta.setCliente(cliente);
        nuevaVenta.setFechaVenta(LocalDateTime.now());

        BigDecimal totalVenta = BigDecimal.ZERO;
        List<VentaItem> itemsParaGuardar = new ArrayList<>();

        // 4. Procesar Items: Validar stock y calcular total
        for (VentaItemRequest itemReq : request.getItems()) {
            Producto producto = productoRepository.findById(itemReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemReq.getProductoId()));

            // 4a. Validar Stock
            if (producto.getStock() < itemReq.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            // 4b. Reducir Stock
            producto.setStock(producto.getStock() - itemReq.getCantidad());
            productoRepository.save(producto); // Guardar el stock actualizado

            // 4c. Crear VentaItem
            VentaItem ventaItem = new VentaItem();
            ventaItem.setVenta(nuevaVenta);
            ventaItem.setProducto(producto);
            ventaItem.setCantidad(itemReq.getCantidad());
            ventaItem.setPrecioUnitario(producto.getPrecio());
            itemsParaGuardar.add(ventaItem);

            // 4d. Acumular Total
            BigDecimal subtotalItem = producto.getPrecio().multiply(BigDecimal.valueOf(itemReq.getCantidad()));
            totalVenta = totalVenta.add(subtotalItem);
        }

        // 5. Guardar la Venta y sus Items
        nuevaVenta.setTotal(totalVenta);
        nuevaVenta.setItems(itemsParaGuardar); // VentaItemRepository no se usa directamente gracias a Cascade
        Venta ventaGuardada = ventaRepository.save(nuevaVenta);

        // 6. Crear el Comprobante (Boleta simple)
        Comprobante comprobante = new Comprobante();
        comprobante.setVenta(ventaGuardada);
        comprobante.setFechaEmision(LocalDateTime.now());
        comprobante.setMoneda("PEN"); 
        comprobante.setRucEmisor("10203040501");

        // 6a. Generar Serie y Número (lógica simple)
        comprobante.setSerie("B001");
        comprobante.setNumero(String.format("%08d", ventaGuardada.getId()));

        // 6b. Cálculo de impuestos
        BigDecimal subtotal = totalVenta.divide(IGV_RATE.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
        BigDecimal igv = totalVenta.subtract(subtotal);

        comprobante.setSubtotal(subtotal);
        comprobante.setIgv(igv);
        comprobante.setTotal(totalVenta);

        comprobanteRepository.save(comprobante);

        // 7. Retornar la respuesta
        return new CheckoutResponse(
                ventaGuardada.getId(),
                comprobante.getSerie(),
                comprobante.getNumero(),
                comprobante.getFechaEmision(),
                comprobante.getTotal()
        );
    }

    @Transactional
    public ComprobanteDTO getVentaComoCliente(Long ventaId, String clienteEmail) {
        
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        if (!venta.getCliente().getEmail().equals(clienteEmail)) {
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
