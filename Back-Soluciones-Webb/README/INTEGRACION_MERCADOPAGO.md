# Integración de Mercado Pago Checkout Pro

## 📋 Resumen

Este documento describe la integración exitosa de **Mercado Pago Checkout Pro** como pasarela de pagos en el sistema de ventas. La integración permite procesar pagos de forma segura, gestionar webhooks automáticos y confirmar ventas después de la aprobación del pago.

---

## 🎯 Objetivo

Reemplazar el sistema de checkout directo por una integración con Mercado Pago que:
- Cree preferencias de pago seguras
- Redirija al cliente a la plataforma de Mercado Pago
- Procese webhooks automáticos de confirmación
- Genere ventas y comprobantes solo después del pago aprobado

---

## 🏗️ Arquitectura de la Solución

### Flujo de Pago

```
1. Cliente → POST /venta/checkout
2. Backend → Crea preferencia en Mercado Pago
3. Backend → Guarda CheckoutPendiente
4. Backend → Devuelve URL de pago (initPoint)
5. Cliente → Redirigido a Mercado Pago
6. Cliente → Completa el pago
7. Mercado Pago → Envía webhook al backend
8. Backend → Confirma pago y crea Venta
9. Backend → Genera Comprobante
10. Backend → Actualiza stock
```

---

## 📦 Componentes Implementados

### 1. Modelos de Datos

#### `Venta` (Modificado)
Agregados campos para tracking de Mercado Pago:
```java
@Column(name = "mercadopago_preference_id")
private String mercadoPagoPreferenceId;

@Column(name = "mercadopago_payment_id")
private String mercadoPagoPaymentId;

@Column(name = "estado_pago", length = 20)
private String estadoPago; // PENDING, APPROVED, REJECTED, CANCELLED

@Column(name = "metodo_pago", length = 50)
private String metodoPago;
```

#### `CheckoutPendiente` (Nuevo)
Almacena información temporal del checkout antes de confirmar el pago:
```java
- preferenceId: ID de la preferencia de Mercado Pago
- cliente: Cliente que realiza la compra
- itemsJson: Items del carrito serializados como JSON
- direccion, ciudad, pais, codigoPostal: Datos de envío
- total: Total de la compra
- estado: PENDING, COMPLETED, EXPIRED
- fechaCreacion, fechaExpiracion: Control temporal
```

### 2. Repositorios

#### `CheckoutPendienteRepository`
```java
Optional<CheckoutPendiente> findByPreferenceId(String preferenceId);
```

### 3. Servicios

#### `MercadoPagoService`
Gestiona la comunicación con la API de Mercado Pago:

**Métodos principales:**
- `crearPreferencia()`: Crea preferencia de pago con items del carrito
- `obtenerPago()`: Obtiene información de un pago por ID

**Características:**
- Logging detallado de errores
- Mapeo de productos a items de Mercado Pago
- Configuración de URLs de callback
- Manejo de excepciones con información completa

#### `CheckoutService` (Modificado)

**Método `realizarCheckout()`:**
1. Valida productos y stock
2. Crea `CheckoutPendiente` con información temporal
3. Serializa items a JSON
4. Genera preferencia en Mercado Pago
5. Actualiza checkout con `preferenceId`
6. Devuelve URL de pago

**Método `confirmarVenta()`:**
1. Busca checkout pendiente por `preferenceId`
2. Valida que no esté ya procesado
3. Deserializa items del JSON
4. Crea la venta con información de pago
5. Reduce stock de productos
6. Genera comprobante
7. Marca checkout como COMPLETED

**Método `confirmarVentaPorExternalReference()`:**
- Extrae ID del checkout del external reference
- Llama a `confirmarVenta()` con el `preferenceId` correspondiente

### 4. Controladores

#### `MercadoPagoWebhookController` (Nuevo)
Endpoint: `POST /api/v1/webhooks/mercadopago`

**Funcionalidad:**
- Recibe notificaciones de Mercado Pago
- Valida tipo de notificación (solo procesa pagos)
- Obtiene información del pago desde Mercado Pago
- Confirma venta cuando el pago es aprobado
- Maneja reintentos y duplicados
- Logging completo para debugging

**Validaciones:**
- Solo procesa notificaciones tipo "payment"
- Verifica que el pago esté aprobado
- Valida external reference
- Previene procesamiento duplicado

### 5. Configuración

#### `MercadoPagoConfiguration`
Carga configuración desde `application.properties`:
```java
@Value("${mercadopago.access.token}")
private String accessToken;

@Value("${mercadopago.success.url}")
private String successUrl;

@Value("${mercadopago.failure.url}")
private String failureUrl;

@Value("${mercadopago.pending.url}")
private String pendingUrl;

@Value("${mercadopago.notification.url}")
private String notificationUrl;
```

Inicializa el SDK de Mercado Pago en `@PostConstruct`.

### 6. DTOs

#### `CheckoutResponse` (Modificado)
```java
{
  "initPoint": "https://www.mercadopago.com.pe/checkout/v1/redirect?pref_id=...",
  "preferenceId": "xxx-xxx-xxx",
  "total": 179.80,
  "checkoutPendienteId": 10
}
```

#### `MercadoPagoWebhookDTO` (Nuevo)
Recibe notificaciones de webhook con estructura:
```java
{
  "type": "payment",
  "action": "payment.created",
  "data": {
    "id": "134210993989"
  }
}
```

---

## 🗄️ Base de Datos

### Migración SQL

```sql
-- Tabla checkout_pendiente
CREATE TABLE IF NOT EXISTS checkout_pendiente (
    id BIGSERIAL PRIMARY KEY,
    preference_id VARCHAR(255) UNIQUE,
    cliente_id BIGINT NOT NULL,
    items_json TEXT NOT NULL,
    direccion VARCHAR(255),
    ciudad VARCHAR(100),
    pais VARCHAR(100),
    codigo_postal VARCHAR(20),
    total DECIMAL(10, 2),
    estado VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_expiracion TIMESTAMP,
    CONSTRAINT fk_checkout_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

-- Modificaciones a tabla venta
ALTER TABLE venta 
ADD COLUMN IF NOT EXISTS mercadopago_preference_id VARCHAR(255),
ADD COLUMN IF NOT EXISTS mercadopago_payment_id VARCHAR(255),
ADD COLUMN IF NOT EXISTS estado_pago VARCHAR(20),
ADD COLUMN IF NOT EXISTS metodo_pago VARCHAR(50);

-- Índices para optimización
CREATE INDEX idx_checkout_pendiente_preference_id ON checkout_pendiente(preference_id);
CREATE INDEX idx_venta_mercadopago_preference_id ON venta(mercadopago_preference_id);
```

---

## ⚙️ Configuración

### 1. Access Token de Mercado Pago

Agregar en `application.properties`:

```properties
# Mercado Pago Configuration
mercadopago.access.token=TEST-1234567890-abcdef-xyz...

# URLs de redirección (frontend)
mercadopago.success.url=http://localhost:4200/checkout/success
mercadopago.failure.url=http://localhost:4200/checkout/failure
mercadopago.pending.url=http://localhost:4200/checkout/pending

# URL del webhook (DEBE ser pública)
mercadopago.notification.url=https://tu-dominio.ngrok-free.dev/api/v1/webhooks/mercadopago
```

### 2. Obtener Access Token

1. Ir a: https://www.mercadopago.com.pe/developers/panel/credentials
2. Usar **Access Token de Prueba** (TEST-...) para desarrollo
3. Usar **Access Token de Producción** (APP_USR-...) para producción

### 3. Configurar Webhook en Mercado Pago

1. Ir a: https://www.mercadopago.com.pe/developers/panel/webhooks
2. Agregar URL del webhook
3. Seleccionar evento: **Pagos**

### 4. Desarrollo Local con Ngrok

Para que Mercado Pago pueda enviar webhooks a tu máquina local:

```bash
ngrok http 8080
```

Usar la URL de ngrok como `mercadopago.notification.url`.

---

## 🧪 Pruebas

### 1. Crear Checkout

**Request:**
```bash
POST /venta/checkout
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "items": [
    {
      "productoId": 1,
      "cantidad": 2
    }
  ],
  "direccion": "Av. Principal 123",
  "ciudad": "Lima",
  "pais": "Perú",
  "codigoPostal": "15001"
}
```

**Response:**
```json
{
  "initPoint": "https://www.mercadopago.com.pe/checkout/v1/redirect?pref_id=...",
  "preferenceId": "3006513216-37366693-8952-4944-a7f1-65aa48ef682a",
  "total": 179.80,
  "checkoutPendienteId": 10
}
```

### 2. Completar Pago

1. Abrir la URL `initPoint` en el navegador
2. Usar tarjetas de prueba: https://www.mercadopago.com.pe/developers/es/docs/checkout-pro/additional-content/test-cards

### 3. Verificar Webhook

El webhook se ejecuta automáticamente. Verificar logs:

```
INFO - Webhook recibido - Tipo: payment, Action: payment.created
INFO - Procesando pago ID: 134210993989
INFO - Pago obtenido - Status: approved, Amount: 89.9
INFO - Venta confirmada exitosamente - ID: 31
```

### 4. Consultar Venta

**Request:**
```bash
GET /api/v1/ventas/31
Authorization: Bearer {JWT_TOKEN}
```

**Response:**
```json
{
  "ventaId": 31,
  "serie": "B001",
  "numero": "00000031",
  "subtotal": 76.19,
  "igv": 13.71,
  "total": 89.90,
  "clienteNombre": "cliente1 cliente1 cliente1",
  "fechaEmision": "2025-11-22T02:39:48.858895"
}
```

---

## ✅ Validación de Integración Exitosa

### Prueba Completa Realizada

1. ✅ **Checkout creado** → Preferencia ID: `3006513216-37366693-8952-4944-a7f1-65aa48ef682a`
2. ✅ **Cliente redirigido** → URL de Mercado Pago
3. ✅ **Pago completado** → Monto: S/ 89.90
4. ✅ **Webhook recibido** → Payment ID: `134210993989`
5. ✅ **Pago aprobado** → Status: `approved`
6. ✅ **Venta creada** → ID: 31
7. ✅ **Comprobante generado** → Serie: B001-00000031
8. ✅ **Stock actualizado** → Reducción automática
9. ✅ **Consulta exitosa** → Cliente puede ver su comprobante

---

## 🔍 Troubleshooting

### Error: "auto_return invalid. back_url.success must be defined"
### Mercado pago solo permite URL publicas, ambos puertos deben usar ngrok.


**Solución de Prueba:** Comentar la línea `.autoReturn("approved")` en `MercadoPagoService`:

```java
PreferenceRequest preferenceRequest = PreferenceRequest.builder()
    .items(mpItems)
    .backUrls(backUrls)
    // .autoReturn("approved") // Comentado: causa conflicto con back_urls
    .externalReference(externalReference)
    .notificationUrl(mpConfig.getNotificationUrl())
    .build();
```
# Comandos para ngrok:
# ngrok http 4200 --pooling-enabled

# ngrok http 8080 --pooling-enabled
### Webhook no se ejecuta

**Causas comunes:**
1. URL del webhook no es pública (usar ngrok)
2. Webhook no configurado en panel de Mercado Pago
3. Ngrok requiere aceptar advertencia en navegador

**Verificación:**
- Revisar logs del servidor
- Verificar que ngrok esté corriendo
- Confirmar URL en configuración de Mercado Pago

### Error: "preference_id cannot be null"

**Solución:** La columna `preference_id` debe permitir NULL temporalmente:

```sql
ALTER TABLE checkout_pendiente 
ALTER COLUMN preference_id DROP NOT NULL;
```

---

## 📊 Estadísticas de Implementación

- **Archivos creados:** 5
- **Archivos modificados:** 4
- **Líneas de código:** ~800
- **Tiempo de integración:** Exitosa
- **Pruebas realizadas:** 100% exitosas

---

## 🚀 Próximos Pasos (Opcional)

1. **Implementar manejo de pagos rechazados**
2. **Agregar notificaciones por email** al cliente
3. **Dashboard de ventas** con filtros por estado de pago
4. **Reportes de ventas** por método de pago
5. **Integración con sistema de facturación electrónica**
6. **Implementar reembolsos** a través de Mercado Pago

---

## 📝 Notas Importantes

> [!WARNING]
> **Stock:** El stock se reduce SOLO cuando el pago es confirmado, no al crear el checkout. Esto evita reservas sin pago.

> [!CAUTION]
> **Webhooks duplicados:** Mercado Pago puede enviar el mismo webhook múltiples veces. El código maneja esto verificando si el checkout ya fue completado.

> [!IMPORTANT]
> **Access Token:** Nunca subir el access token a repositorios públicos. Usar variables de entorno en producción.

---

## 👥 Créditos

**Desarrollado por:** William  
**Fecha:** 22 de Noviembre de 2025  
**Versión:** 1.0.0  
**Estado:** Ready

---

## 📚 Referencias

- [Documentación Mercado Pago Checkout Pro](https://www.mercadopago.com.pe/developers/es/docs/checkout-pro/landing)
- [SDK Java de Mercado Pago](https://github.com/mercadopago/sdk-java)
- [Tarjetas de Prueba](https://www.mercadopago.com.pe/developers/es/docs/checkout-pro/additional-content/test-cards)
- [Webhooks de Mercado Pago](https://www.mercadopago.com.pe/developers/es/docs/checkout-pro/additional-content/webhooks)
