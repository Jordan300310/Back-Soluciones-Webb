package com.example.web.service.admin;

import com.example.web.dto.compra.*;
import com.example.web.models.compra.Compra;
import com.example.web.models.compra.CompraItem;
import com.example.web.models.Producto.Producto;
import com.example.web.models.Proveedor.Proveedor;
import com.example.web.repository.compra.CompraItemRepository;
import com.example.web.repository.compra.CompraRepository;
import com.example.web.repository.producto.ProductoRepository;
import com.example.web.repository.proveedor.ProveedorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminCompraService {

  private static final BigDecimal IGV_RATE = new BigDecimal("0.18");

  private final CompraRepository compraRepo;
  private final CompraItemRepository itemRepo;
  private final ProductoRepository productoRepo;
  private final ProveedorRepository proveedorRepo;

  public AdminCompraService(CompraRepository compraRepo,
                            CompraItemRepository itemRepo,
                            ProductoRepository productoRepo,
                            ProveedorRepository proveedorRepo) {
    this.compraRepo = compraRepo;
    this.itemRepo = itemRepo;
    this.productoRepo = productoRepo;
    this.proveedorRepo = proveedorRepo;
  }

  @Transactional
  public CompraCreatedDTO crear(CompraRequest req) {
    if (req == null || req.idProveedor() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Proveedor requerido");
    }
    Proveedor prov = proveedorRepo.findById(req.idProveedor())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Proveedor no existe"));

    if (req.items() == null || req.items().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Items requeridos");
    }

    // Header
    Compra compra = new Compra();
    compra.setIdProveedor(prov.getId());
    compra = compraRepo.save(compra);

    BigDecimal subtotal = BigDecimal.ZERO;

    // Items + stock
    for (CompraItemRequest it : req.items()) {
      if (it.idProducto() == null || it.cantidad() == null || it.cantidad() <= 0 || it.costoUnit() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item inválido");
      }

      Producto p = productoRepo.findById(it.idProducto())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no existe: " + it.idProducto()));

      // sumar stock
      int nuevoStock = (p.getStock() == null ? 0 : p.getStock()) + it.cantidad();
      p.setStock(nuevoStock);
      productoRepo.save(p);

      // guardar item
      CompraItem ci = new CompraItem();
      ci.setIdCompra(compra.getId());
      ci.setIdProducto(p.getId());
      ci.setCantidad(it.cantidad());
      ci.setCostoUnit(it.costoUnit());
      BigDecimal line = it.costoUnit().multiply(BigDecimal.valueOf(it.cantidad()));
      ci.setTotalLinea(line);
      itemRepo.save(ci);

      subtotal = subtotal.add(line);
    }

    BigDecimal igv = subtotal.multiply(IGV_RATE).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal total = subtotal.add(igv);

    compra.setSubtotal(subtotal);
    compra.setIgv(igv);
    compra.setTotal(total);
    compraRepo.save(compra);

    return new CompraCreatedDTO(compra.getId(), subtotal, igv, total);
  }

  @Transactional(readOnly = true)
public List<CompraListDTO> listar() {
  return compraRepo.findAll().stream()
      .sorted((a,b) -> b.getId().compareTo(a.getId()))
      .map(c -> {
        var proveedor = proveedorRepo.findById(c.getIdProveedor())
            .map(Proveedor::getRazonSocial).orElse(null);

        var items = itemRepo.findByIdCompra(c.getId());
        String productos = items.stream()
            .map(it -> {
              String nombre = productoRepo.findById(it.getIdProducto())
                  .map(Producto::getNombre)
                  .orElse("#"+it.getIdProducto());
              return nombre + " x" + it.getCantidad();
            })
            .collect(java.util.stream.Collectors.joining(", "));

        return new CompraListDTO(
            c.getId(), c.getIdProveedor(), proveedor,
            c.getCreadoEn(), c.getSubtotal(), c.getIgv(), c.getTotal(),
            productos
        );
      })
      .toList();
}
}