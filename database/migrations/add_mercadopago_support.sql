-- Migración para agregar soporte de Mercado Pago
-- Ejecutar este script en tu base de datos PostgreSQL

-- 1. Crear tabla checkout_pendiente
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

-- 2. Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_checkout_pendiente_preference_id ON checkout_pendiente(preference_id);
CREATE INDEX IF NOT EXISTS idx_checkout_pendiente_cliente_id ON checkout_pendiente(cliente_id);
CREATE INDEX IF NOT EXISTS idx_checkout_pendiente_estado ON checkout_pendiente(estado);

-- 3. Agregar columnas de Mercado Pago a la tabla venta
ALTER TABLE venta 
ADD COLUMN IF NOT EXISTS mercadopago_preference_id VARCHAR(255),
ADD COLUMN IF NOT EXISTS mercadopago_payment_id VARCHAR(255),
ADD COLUMN IF NOT EXISTS estado_pago VARCHAR(20),
ADD COLUMN IF NOT EXISTS metodo_pago VARCHAR(50);

-- 4. Crear índices para las nuevas columnas
CREATE INDEX IF NOT EXISTS idx_venta_mercadopago_preference_id ON venta(mercadopago_preference_id);
CREATE INDEX IF NOT EXISTS idx_venta_mercadopago_payment_id ON venta(mercadopago_payment_id);
CREATE INDEX IF NOT EXISTS idx_venta_estado_pago ON venta(estado_pago);

-- 5. Comentarios para documentación
COMMENT ON TABLE checkout_pendiente IS 'Almacena checkouts pendientes de confirmación de pago de Mercado Pago';
COMMENT ON COLUMN checkout_pendiente.preference_id IS 'ID de la preferencia de pago en Mercado Pago';
COMMENT ON COLUMN checkout_pendiente.items_json IS 'Items del carrito serializados como JSON';
COMMENT ON COLUMN checkout_pendiente.estado IS 'Estado del checkout: PENDING, COMPLETED, EXPIRED';

COMMENT ON COLUMN venta.mercadopago_preference_id IS 'ID de la preferencia de pago en Mercado Pago';
COMMENT ON COLUMN venta.mercadopago_payment_id IS 'ID del pago confirmado en Mercado Pago';
COMMENT ON COLUMN venta.estado_pago IS 'Estado del pago: PENDING, APPROVED, REJECTED, CANCELLED';
COMMENT ON COLUMN venta.metodo_pago IS 'Método de pago utilizado (tarjeta, efectivo, etc.)';
-- 6. Índices adicionales para optimización de consultas
CREATE INDEX IF NOT EXISTS idx_venta_fecha_venta ON venta(fecha_venta);
CREATE INDEX IF NOT EXISTS idx_venta_estado_pago ON venta(estado_pago);
CREATE INDEX IF NOT EXISTS idx_venta_metodo_pago ON venta(metodo_pago);

CREATE INDEX IF NOT EXISTS idx_venta_item_venta_id ON venta_item(venta_id);
CREATE INDEX IF NOT EXISTS idx_venta_item_producto_id ON venta_item(producto_id);

CREATE INDEX IF NOT EXISTS idx_producto_stock ON producto(stock);

CREATE INDEX IF NOT EXISTS idx_checkout_estado ON checkout_pendiente(estado);
CREATE INDEX IF NOT EXISTS idx_checkout_fecha_creacion ON checkout_pendiente(fecha_creacion);