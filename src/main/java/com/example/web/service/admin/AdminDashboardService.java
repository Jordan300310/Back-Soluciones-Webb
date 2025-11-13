package com.example.web.service.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.web.dto.admin.DashboardResponse;
import com.example.web.dto.admin.TopProductoDTO;
import com.example.web.repository.venta.VentaItemRepository;
import com.example.web.repository.venta.VentaRepository;

@Service
public class AdminDashboardService {

    private final VentaRepository ventaRepository;
    private final VentaItemRepository ventaItemRepository;

    public AdminDashboardService(VentaRepository ventaRepository, VentaItemRepository ventaItemRepository) {
        this.ventaRepository = ventaRepository;
        this.ventaItemRepository = ventaItemRepository;
    }

    public DashboardResponse getDashboardForCurrentMonth() {
        LocalDate now = LocalDate.now();
        LocalDateTime start = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1).minusNanos(1);

        BigDecimal ganancias = ventaRepository.sumTotalBetween(start, end);
        Long ventasCount = ventaRepository.countByFechaVentaBetween(start, end);

        // Top productos (limit 5)
        List<Object[]> rows = ventaItemRepository.findTopProductosBetween(start, end, PageRequest.of(0, 5));
        List<TopProductoDTO> top = new ArrayList<>();
        for (Object[] r : rows) {
            Long productoId = r[0] == null ? null : ((Number) r[0]).longValue();
            String nombre = r[1] == null ? null : r[1].toString();
            Long cantidad = r[2] == null ? 0L : ((Number) r[2]).longValue();
            java.math.BigDecimal total = r[3] == null ? BigDecimal.ZERO : (java.math.BigDecimal) r[3];
            top.add(new TopProductoDTO(productoId, nombre, cantidad, total));
        }

        return new DashboardResponse(ventasCount, ganancias, top);
    }
}
