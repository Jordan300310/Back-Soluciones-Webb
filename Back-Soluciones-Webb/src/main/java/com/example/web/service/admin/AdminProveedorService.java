package com.example.web.service.admin;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.dto.common.ComboDTO;
import com.example.web.models.Proveedor.Proveedor;
import com.example.web.repository.proveedor.ProveedorRepository;

import java.util.List;

@Service
public class AdminProveedorService {

  private final ProveedorRepository repo;

  public AdminProveedorService(ProveedorRepository repo) {
    this.repo = repo;

    
  }

    private boolean isValidRuc(String ruc) {
    if (ruc == null) return false;
    ruc = ruc.trim();
    if (!ruc.matches("\\d{11}")) return false;

    int[] factors = {5,4,3,2,7,6,5,4,3,2};
    int sum = 0;
    for (int i = 0; i < 10; i++) {
      int d = Character.digit(ruc.charAt(i), 10);
      sum += d * factors[i];
    }
    int remainder = sum % 11;
    int check = 11 - remainder;
    int dv = (check == 10) ? 0 : (check == 11 ? 1 : check);
    int last = Character.digit(ruc.charAt(10), 10);
    return dv == last;
  }

  @Transactional
  public Proveedor create(Proveedor p) {
    if (p.getRuc() == null || p.getRuc().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RUC requerido");
    }

    if (!isValidRuc(p.getRuc())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RUC inválido");
    }

    if (repo.findByRuc(p.getRuc().trim()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "RUC ya registrado");
    }

    if (p.getEstado() == null) {
      p.setEstado(true);
    }
    return repo.save(p);
  }
     @Transactional(readOnly = true)
    public List<ComboDTO> listarActivosCombo() {
        return repo.findAll().stream()
            .filter(p -> Boolean.TRUE.equals(p.getEstado()))
            .map(p -> new ComboDTO(
                p.getId(),
                p.getRazonSocial() 
            ))
            .toList();
    }

  @Transactional(readOnly = true)
  public List<Proveedor> list() {
    return repo.findByEstadoTrue();
  }

  @Transactional(readOnly = true)
  public Proveedor get(Long id) {
    return repo.findById(id).orElse(null);
  }

   @Transactional
  public Proveedor update(Long id, Proveedor in) {
    Proveedor db = repo.findById(id).orElse(null);
    if (db == null) return null;

    if (in.getRazonSocial() != null) db.setRazonSocial(in.getRazonSocial());

    if (in.getRuc() != null) {
      String ruc = in.getRuc().trim();
      if (!isValidRuc(ruc)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RUC inválido");
      }
      var other = repo.findByRuc(ruc);
      if (other.isPresent() && !other.get().getId().equals(db.getId())) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "RUC ya registrado");
      }
      db.setRuc(ruc);
    }

    if (in.getCel() != null)   db.setCel(in.getCel());
    if (in.getEmail() != null) db.setEmail(in.getEmail());

    return repo.save(db);
  }

  @Transactional
  public void delete(Long id) {
    Proveedor db = repo.findById(id).orElse(null);
    if (db != null) {
      db.setEstado(false);
      repo.save(db);
    }
  }
}
