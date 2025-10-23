package com.example.web.controller.publico;

import org.springframework.web.bind.annotation.*;
import com.example.web.dto.admin.ProductoAdminDTO;
import com.example.web.service.admin.AdminProductoService;
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
}
