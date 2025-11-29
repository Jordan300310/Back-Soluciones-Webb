package com.example.web.controller.publico;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.web.dto.admin.ProductoAdminDTO;
import com.example.web.models.Producto.Producto;
import com.example.web.service.admin.AdminProductoService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/publico/productos")
public class PublicProductoController {

  private final AdminProductoService service;

  public PublicProductoController(AdminProductoService service) {
    this.service = service;
  }

  @GetMapping
  public List<ProductoAdminDTO> listarPublicos() {
    return service.list().stream()
        .map(ProductoAdminDTO::of)
        .toList();
  }

  @GetMapping("/{id}")
  public ProductoAdminDTO getProducto(@PathVariable Long id) {
    var p = service.get(id);
    if (p == null) {
      throw new RuntimeException("Producto no encontrado");
    }
    return ProductoAdminDTO.of(p);
  }
  
  @GetMapping("/top5masvendidos")
  public List<ProductoAdminDTO> top5MasVendidos() {
    return service.top5MasVendidos().stream()
        .map(ProductoAdminDTO::of)
        .toList();
  }
  @GetMapping("/filtrar")
    public ResponseEntity<List<ProductoAdminDTO>> filtrar(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) List<String> marcas,
            @RequestParam(required = false) List<String> categorias,
            @RequestParam(required = false) BigDecimal min,
            @RequestParam(required = false) BigDecimal max
    ) {
        List<Producto> productos = service.filtrarProductos(texto, marcas, categorias, min, max);
        
        return ResponseEntity.ok(productos.stream()
                .map(ProductoAdminDTO::of)
                .toList());
    }
}