package com.example.web.service.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public DashboardResponse getDashboardData(String fechaInicioStr, String fechaFinStr) {
        
        // 1. Lógica de Fechas
        LocalDateTime start;
        LocalDateTime end;

        if (fechaInicioStr != null && fechaFinStr != null) {
            // Caso: El usuario seleccionó un rango en el calendario
            LocalDate fi = LocalDate.parse(fechaInicioStr);
            LocalDate ff = LocalDate.parse(fechaFinStr);
            start = fi.atStartOfDay();
            end = ff.atTime(LocalTime.MAX);
        } else {
            // Caso: Default (Carga inicial) -> Mes Actual
            LocalDate now = LocalDate.now();
            start = now.withDayOfMonth(1).atStartOfDay();
            end = start.plusMonths(1).minusNanos(1);
        }

        // 2. Métricas Generales (Usando el rango definido arriba)
        BigDecimal ganancias = ventaRepository.sumTotalBetween(start, end);
        // Manejo de null por si no hay ventas en ese rango
        if (ganancias == null) ganancias = BigDecimal.ZERO; 
        
        Long ventasCount = ventaRepository.countByFechaVentaBetween(start, end);

        // 3. Top Productos (Usando el rango)
        List<Object[]> rows = ventaItemRepository.findTopProductosBetween(start, end, PageRequest.of(0, 5));
        List<TopProductoDTO> top = mapTopProductos(rows);

        DashboardResponse response = new DashboardResponse(ventasCount, ganancias, top);

        // 4. GRÁFICO DE LÍNEA DE TIEMPO (Ventas por periodo seleccionado)
        // Esto alimentará tu gráfico de línea superior
        List<Object[]> rowsDias = ventaRepository.findVentasPorRango(start, end); // Usar la query nueva del paso 3
        List<VentaDiariaDTO> dias = new ArrayList<>();
        for (Object[] r : rowsDias) {
            // Asumiendo que r[0] viene como Date o Timestamp, convertir a String
            String fecha = r[0].toString(); 
            BigDecimal total = r[1] == null ? BigDecimal.ZERO : (BigDecimal)r[1];
            dias.add(new VentaDiariaDTO(fecha, total));
        }
        response.setVentasPorPeriodo(dias);

        // 5. Categorías (Usando el rango - OJO: Necesitas actualizar esta query en el repo también)
        // Deberías crear un findVentasPorCategoriaBetween(start, end) en tu repo
        // Si tu query actual no filtra por fecha, mostrará datos históricos totales, lo cual está mal para un filtro por mes.
        List<Object[]> rowsCat = ventaItemRepository.findVentasPorCategoriaBetween(start, end); 
        List<VentaCategoriaDTO> cats = new ArrayList<>();
        for (Object[] r : rowsCat) {
             String catName = r[0] == null ? "Otros" : r[0].toString();
             BigDecimal total = r[1] == null ? BigDecimal.ZERO : (BigDecimal)r[1];
             cats.add(new VentaCategoriaDTO(catName, total));
        }
        response.setVentasPorCategoria(cats);

        // 6. Stock Bajo (Esto es una foto del momento actual, NO depende de fechas históricas)
        // Se mantiene igual, ya que el stock es el que hay HOY.
        List<Producto> lowStock = productoRepository.findByStockLessThanAndEstadoTrueOrderByStockAsc(10);
        List<ProductoResumenDTO> lowStockDTO = lowStock.stream()
            .map(p -> new ProductoResumenDTO(p.getId(), p.getNombre(), p.getStock(), p.getPrecio())) 
            .limit(5)
            .toList();
        response.setProductosBajoStock(lowStockDTO);

        // 7. Productos Sin Ventas (Zombies)
        // Aquí podrías decidir: ¿Sin ventas en TODA la historia o sin ventas en el RANGO seleccionado?
        // Usualmente "Zombies" son productos que no se mueven hace X tiempo fijo (ej. 30 días atrás desde hoy).
        // Lo dejaremos relativo a HOY para detectar productos estancados reales.
        LocalDateTime hace30dias = LocalDateTime.now().minusDays(30);
        List<Producto> zombies = productoRepository.findProductosSinVentasDesde(hace30dias);
        List<ProductoResumenDTO> zombiesDTO = zombies.stream()
            .map(p -> new ProductoResumenDTO(p.getId(), p.getNombre(), p.getStock(), p.getPrecio()))
            .limit(5)
            .toList();
        response.setProductosSinVentas(zombiesDTO);

        return response;
    }

    // Helper para mapear (para no repetir código)
    private List<TopProductoDTO> mapTopProductos(List<Object[]> rows) {
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

    // Actualizar detalle categoría para que respete las fechas también
    public List<TopProductoDTO> getDetalleCategoria(String categoria, String fInicio, String fFin) {
        LocalDateTime start;
        LocalDateTime end;
        
        if (fInicio != null && fFin != null) {
            start = LocalDate.parse(fInicio).atStartOfDay();
            end = LocalDate.parse(fFin).atTime(LocalTime.MAX);
        } else {
             LocalDate now = LocalDate.now();
             start = now.withDayOfMonth(1).atStartOfDay();
             end = now.plusMonths(1).atStartOfDay().minusNanos(1);
        }

        List<Object[]> rows = ventaItemRepository.findTopProductosByCategoria(start, end, categoria);
        return mapTopProductos(rows);
    }
}