package com.example.web.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.web.dto.common.ComboDTO;
import com.example.web.models.Producto.Producto;
import com.example.web.repository.producto.ProductoRepository;

import java.util.List;

@Service
public class AdminProductoService {

  private final ProductoRepository repo;

  public AdminProductoService(ProductoRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public Producto create(Producto p) {
    if (p.getEstado() == null) {
      p.setEstado(true);
    }
    return repo.save(p);
  }
  @Transactional(readOnly = true)
    public List<ComboDTO> listarActivosCombo() {
        return repo.findAll().stream()
            // 🔴 CAMBIA getEstado() por getActivo() o el boolean que tengas
            .filter(p -> Boolean.TRUE.equals(p.getEstado()))
            .map(p -> new ComboDTO(
                p.getId(),
                p.getNombre()
            ))
            .toList();
    }

   @Transactional(readOnly = true)
  public List<Producto> list() {
    return repo.findByEstadoTrue();
  }

  @Transactional(readOnly = true)
  public Producto get(Long id) {
    return repo.findById(id).orElse(null);
  }

  @Transactional
  public Producto update(Long id, Producto in) {
    Producto db = repo.findById(id).orElse(null);
    if (db == null) return null;

    if (in.getNombre() != null)      db.setNombre(in.getNombre());
    if (in.getDescripcion() != null) db.setDescripcion(in.getDescripcion());
    if (in.getPrecio() != null)      db.setPrecio(in.getPrecio());
    if (in.getStock() != null)       db.setStock(in.getStock());
    if (in.getMarca() != null)       db.setMarca(in.getMarca());
    if (in.getCategoria() != null)   db.setCategoria(in.getCategoria());
    if (in.getIdProveedor() != null) db.setIdProveedor(in.getIdProveedor());
    if (in.getImagenUrl() != null)   db.setImagenUrl(in.getImagenUrl());
    return repo.save(db);
  }

  @Transactional
  public void delete(Long id) {
    Producto db = repo.findById(id).orElse(null);
    if (db != null) {
      db.setEstado(false);
      repo.save(db);
    }
  }
}
