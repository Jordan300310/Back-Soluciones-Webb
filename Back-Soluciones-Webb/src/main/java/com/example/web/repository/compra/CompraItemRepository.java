package com.example.web.repository.compra;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web.models.compra.CompraItem;
import java.util.List;
public interface CompraItemRepository extends JpaRepository<CompraItem, Long> {
     List<CompraItem> findByIdCompra(Long idCompra);
}