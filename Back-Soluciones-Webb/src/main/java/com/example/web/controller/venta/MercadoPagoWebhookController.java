package com.example.web.controller.venta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.web.dto.venta.MercadoPagoWebhookDTO;
import com.example.web.models.venta.Venta;
import com.example.web.service.venta.CheckoutService;
import com.example.web.service.venta.MercadoPagoService;
import com.mercadopago.resources.payment.Payment;

@RestController
@RequestMapping("/api/v1/webhooks/mercadopago")
public class MercadoPagoWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(MercadoPagoWebhookController.class);

    private final CheckoutService checkoutService;
    private final MercadoPagoService mercadoPagoService;

    public MercadoPagoWebhookController(
            CheckoutService checkoutService,
            MercadoPagoService mercadoPagoService) {
        this.checkoutService = checkoutService;
        this.mercadoPagoService = mercadoPagoService;
    }

    /**
     * Endpoint para recibir notificaciones de Mercado Pago
     */
    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody MercadoPagoWebhookDTO webhook) {
        try {
            logger.info("Webhook recibido - Tipo: {}, Action: {}", webhook.getType(), webhook.getAction());

            if (!"payment".equals(webhook.getType())) {
                logger.info("Tipo de notificación ignorada: {}", webhook.getType());
                return ResponseEntity.ok("Notificación ignorada");
            }

            String paymentIdStr = webhook.getData().getId();
            if (paymentIdStr == null || paymentIdStr.isEmpty()) {
                logger.warn("Webhook sin ID de pago");
                return ResponseEntity.badRequest().body("ID de pago no encontrado");
            }

            Long paymentId = Long.parseLong(paymentIdStr);
            logger.info("Procesando pago ID: {}", paymentId);

            Payment payment = mercadoPagoService.obtenerPago(paymentId);

            logger.info("Estado del pago: {}, Preferencia: {}",
                    payment.getStatus(),
                    payment.getExternalReference());

            if ("approved".equals(payment.getStatus())) {
                String externalReference = payment.getExternalReference();

                if (externalReference != null && externalReference.startsWith("CHECKOUT-")) {
                    try {
                        String checkoutIdStr = externalReference.substring("CHECKOUT-".length());
                        Long checkoutPendienteId = Long.parseLong(checkoutIdStr);

                        logger.info("Buscando checkout pendiente con ID: {}", checkoutPendienteId);

                        Venta venta = checkoutService.confirmarVentaPorExternalReference(
                                externalReference,
                                paymentId.toString(),
                                payment.getStatus(),
                                payment.getPaymentMethodId());

                        logger.info("Venta confirmada exitosamente - ID: {}", venta.getId());
                        return ResponseEntity.ok("Pago procesado exitosamente");

                    } catch (NumberFormatException e) {
                        logger.error("Error al parsear ID de checkout del external reference: {}", externalReference,
                                e);
                        return ResponseEntity.badRequest().body("External reference inválido");
                    }
                } else {
                    logger.warn("External reference inválido: {}", externalReference);
                    return ResponseEntity.badRequest().body("External reference inválido");
                }
            } else {
                logger.info("Pago no aprobado, estado: {}", payment.getStatus());
                return ResponseEntity.ok("Pago no aprobado");
            }

        } catch (NumberFormatException e) {
            logger.error("Error al parsear ID de pago", e);
            return ResponseEntity.badRequest().body("ID de pago inválido");
        } catch (RuntimeException e) {
            logger.error("Error al procesar webhook: {}", e.getMessage(), e);

            if (e.getMessage().contains("ya fue procesado")) {
                return ResponseEntity.ok("Checkout ya procesado");
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar webhook: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al procesar webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado");
        }
    }

    /**
     * Endpoint GET para validación de webhook
     */
    @GetMapping
    public ResponseEntity<String> validateWebhook() {
        return ResponseEntity.ok("Webhook endpoint activo");
    }
}
