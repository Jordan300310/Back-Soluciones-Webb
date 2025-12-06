# Cambios realizados para el Dashboard (Admin/Empleado)

Este documento resume los cambios hechos en el backend para exponer un endpoint de dashboard que el frontend puede consumir para mostrar métricas (ventas del mes, ganancias y productos más vendidos).

Fecha: 13 de noviembre de 2025

## Objetivo

Proveer un endpoint sencillo y seguro que entregue las métricas requeridas por el dashboard del panel administrativo/empleado:

- Número de ventas del mes
- Ganancias del mes (suma de `venta.total`)
- Productos más vendidos (top 5 por defecto)

## Endpoints expuestos

- GET `/admin/dashboard`
  - Headers: `Authorization: Bearer <token>`
  - Respuesta 200 (application/json) con el siguiente contrato:

```json
{
  "ventasMes": 20,
  "gananciasMes": 1878.7,
  "topProductos": [
    {
      "productoId": 1,
      "nombre": "Casco de seguridad MSA V-Gard",
      "cantidadVendida": 8,
      "totalVenta": 719.2
    },
    {
      "productoId": 7,
      "nombre": "Orejeras 3M Peltor Optime 105",
      "cantidadVendida": 2,
      "totalVenta": 198.0
    },
    {
      "productoId": 4,
      "nombre": "Lentes de seguridad UVEX Astrospec",
      "cantidadVendida": 2,
      "totalVenta": 90.0
    },
    {
      "productoId": 10,
      "nombre": "Cono de seguridad 75 cm",
      "cantidadVendida": 2,
      "totalVenta": 138.0
    },
    {
      "productoId": 2,
      "nombre": "Guantes de nitrilo reforzado",
      "cantidadVendida": 1,
      "totalVenta": 12.5
    }
  ]
}
```

Notas:

- Actualmente el endpoint devuelve métricas para el mes corriente.
- El header Authorization debe contener el token devuelto por `POST /auth/login`.
- El token funciona para roles `EMPLEADO` o `ADMIN`, según la configuración actual del proyecto.

## Archivos añadidos/modificados

Se resumen los cambios por fichero y propósito.

- `src/main/java/com/example/web/dto/admin/TopProductoDTO.java`

  - DTO que representa un producto en el ranking: `productoId`, `nombre`, `cantidadVendida`, `totalVenta`.

- `src/main/java/com/example/web/dto/admin/DashboardResponse.java`

  - DTO principal devuelto por el endpoint con `ventasMes`, `gananciasMes` y `List<TopProductoDTO>`.

- `src/main/java/com/example/web/service/admin/AdminDashboardService.java`

  - Servicio que encapsula la lógica de agregación:
    - Calcula suma de `venta.total` para el periodo (mes actual).
    - Cuenta ventas en el periodo.
    - Obtiene top productos (query agregada en `VentaItemRepository`) y mapea a `TopProductoDTO`.

- `src/main/java/com/example/web/controller/admin/AdminDashboardController.java`

  - Controlador REST con `@RequestMapping("/admin/dashboard")` y método `GET` que valida autorización vía `GuardService` y devuelve `DashboardResponse`.

- `src/main/java/com/example/web/repository/venta/VentaRepository.java` (modificado)

  - Agregadas dos consultas JPQL:
    - `BigDecimal sumTotalBetween(LocalDateTime start, LocalDateTime end)`
    - `Long countByFechaVentaBetween(LocalDateTime start, LocalDateTime end)`

- `src/main/java/com/example/web/repository/venta/VentaItemRepository.java` (modificado)
  - Añadida consulta para agrupar por producto y obtener `producto.id`, `producto.nombre`, `SUM(cantidad)` y `SUM(precioUnitario * cantidad)` entre fechas y con orden por cantidad descendente. Retorna `List<Object[]>` que se mapea en el servicio.

## Lógica interna (resumen técnico)

- El `AdminDashboardService#getDashboardForCurrentMonth()` construye `start` y `end` para el mes corriente:
  - `start = primer día del mes a las 00:00`
  - `end = start.plusMonths(1).minusNanos(1)`
- Llama a `ventaRepository.sumTotalBetween(start,end)` para obtener ganancias (BigDecimal).
- Llama a `ventaRepository.countByFechaVentaBetween(start,end)` para obtener la cantidad de ventas.
- Llama a `ventaItemRepository.findTopProductosBetween(start,end, PageRequest.of(0,5))` para los top 5.
- Mapea las filas `Object[]` a `TopProductoDTO` concasts seguros: `Number` -> `long` y `BigDecimal` para totales.

## Posibles mejoras (para el roadmap)

- Soportar query params `?from=YYYY-MM-DD&to=YYYY-MM-DD&top=N` para consultar rangos arbitrarios.
- Añadir endpoint `GET /admin/dashboard/sales-by-day` devuelva serie temporal por día (útil para gráficos de líneas).
- Cambiar la consulta JPQL para devolver un DTO directamente (constructor expression) en lugar de `Object[]` para tipado fuerte.
- Agregar pruebas unitarias para `AdminDashboardService` (happy path y casos con sin ventas).
- Añadir cache (por ejemplo con `@Cacheable`) si las consultas se vuelven costosas.

## Notas operacionales

- El endpoint asume que la moneda base es PEN (ver `Comprobante` creado en `CheckoutService`). Formatear en frontend con `Intl.NumberFormat('es-PE', { style: 'currency', currency: 'PEN' })`.
- La autorización reutiliza `GuardService.requireAdmin(...)` presente en controladores admin; actualmente los tokens con rol `EMPLEADO` funcionan según la configuración actual del proyecto.
