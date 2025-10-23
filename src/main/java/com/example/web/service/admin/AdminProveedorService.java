package com.example.web.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.web.models.Proveedor.Proveedor;
import com.example.web.repository.proveedor.ProveedorRepository;

import java.util.List;

@Service
public class AdminProveedorService {
  private final ProveedorRepository repo;
  public AdminProveedorService(ProveedorRepository repo) { this.repo = repo; }

  @Transactional public Proveedor create(Proveedor p) { return repo.save(p); }
  @Transactional(readOnly = true) public List<Proveedor> list() { return repo.findAll(); }
  @Transactional(readOnly = true) public Proveedor get(Long id) { return repo.findById(id).orElse(null); }

  @Transactional
  public Proveedor update(Long id, Proveedor in) {
    Proveedor db = repo.findById(id).orElse(null); if (db == null) return null;
    if (in.getRazonSocial() != null) db.setRazonSocial(in.getRazonSocial());
    if (in.getRuc() != null)         db.setRuc(in.getRuc());
    if (in.getCel() != null)         db.setCel(in.getCel());
    if (in.getEmail() != null)       db.setEmail(in.getEmail());
    if (in.getEstado() != null)      db.setEstado(in.getEstado());
    return repo.save(db);
  }

  @Transactional public void delete(Long id) { repo.deleteById(id); }
}
