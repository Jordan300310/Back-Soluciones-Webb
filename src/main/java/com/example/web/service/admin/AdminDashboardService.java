package com.example.web.service.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.example.web.repository.producto.ProductoRepository;
import com.example.web.dto.admin.VentaDiariaDTO;
import com.example.web.dto.admin.VentaCategoriaDTO;
import com.example.web.dto.admin.ProductoResumenDTO;
import com.example.web.models.Producto.Producto; // Asegúrate que el import sea correcto (a veces es models.Producto)
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
    private final ProductoRepository productoRepository;

    public AdminDashboardService(VentaRepository ventaRepository, VentaItemRepository ventaItemRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.ventaItemRepository = ventaItemRepository;
        this.productoRepository = productoRepository;
    }

    public DashboardResponse getDashboardForCurrentMonth() {
        LocalDate now = LocalDate.now();
        LocalDateTime start = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1).minusNanos(1);

        BigDecimal ganancias = ventaRepository.sumTotalBetween(start, end);
        Long ventasCount = ventaRepository.countByFechaVentaBetween(start, end);

        // Top Productos General
        List<Object[]> rows = ventaItemRepository.findTopProductosBetween(start, end, PageRequest.of(0, 5));
        List<TopProductoDTO> top = new ArrayList<>();
        for (Object[] r : rows) {
            Long productoId = r[0] == null ? null : ((Number) r[0]).longValue();
            String nombre = r[1] == null ? null : r[1].toString();
            Long cantidad = r[2] == null ? 0L : ((Number) r[2]).longValue();
            BigDecimal total = r[3] == null ? BigDecimal.ZERO : (BigDecimal) r[3];
            top.add(new TopProductoDTO(productoId, nombre, cantidad, total));
        }

        DashboardResponse response = new DashboardResponse(ventasCount, ganancias, top);

        // --- 2. Ventas últimos 7 días ---
        LocalDateTime hace7Dias = LocalDateTime.now().minusDays(7);
        List<Object[]> rowsDias = ventaRepository.findVentasUltimosDias(hace7Dias);
        List<VentaDiariaDTO> dias = new ArrayList<>();
        for (Object[] r : rowsDias) {
            dias.add(new VentaDiariaDTO(r[0].toString(), (BigDecimal)r[1]));
        }
        response.setVentasUltimos7Dias(dias);

        // --- 3. Por Categoría ---
        List<Object[]> rowsCat = ventaItemRepository.findVentasPorCategoria();
        List<VentaCategoriaDTO> cats = new ArrayList<>();
        for (Object[] r : rowsCat) {
            String catName = r[0] == null ? "Otros" : r[0].toString();
            BigDecimal total = r[1] == null ? BigDecimal.ZERO : (BigDecimal)r[1];
            cats.add(new VentaCategoriaDTO(catName, total));
        }
        response.setVentasPorCategoria(cats);

        // --- 4. Stock Bajo ---
        List<Producto> lowStock = productoRepository.findByStockLessThanAndEstadoTrueOrderByStockAsc(10);
        List<ProductoResumenDTO> lowStockDTO = lowStock.stream()
            .map(p -> new ProductoResumenDTO(p.getId(), p.getNombre(), p.getStock(), p.getPrecio())) 
            .limit(5)
            .toList();
        response.setProductosBajoStock(lowStockDTO);

        // --- 5. Productos Zombies ---
        LocalDateTime hace30dias = LocalDateTime.now().minusDays(30);
        List<Producto> zombies = productoRepository.findProductosSinVentasDesde(hace30dias);
        List<ProductoResumenDTO> zombiesDTO = zombies.stream()
            .map(p -> new ProductoResumenDTO(p.getId(), p.getNombre(), p.getStock(), p.getPrecio()))
            .limit(5)
            .toList();
        response.setProductosSinVentas(zombiesDTO);

        return response;
    }

    public List<TopProductoDTO> getDetalleCategoria(String categoria) {
        LocalDate now = LocalDate.now();
        LocalDateTime start = now.withDayOfYear(1).atStartOfDay();
        LocalDateTime end = now.plusDays(1).atStartOfDay();

        List<Object[]> rows = ventaItemRepository.findTopProductosByCategoria(start, end, categoria);

        List<TopProductoDTO> lista = new ArrayList<>();
        for (Object[] r : rows) {
            Long productoId = r[0] == null ? null : ((Number) r[0]).longValue();
            String nombre = r[1] == null ? null : r[1].toString();
            Long cantidad = r[2] == null ? 0L : ((Number) r[2]).longValue();
            BigDecimal total = r[3] == null ? BigDecimal.ZERO : (BigDecimal) r[3];
            
            lista.add(new TopProductoDTO(productoId, nombre, cantidad, total));
        }
        return lista;
    }
}