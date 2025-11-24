package com.example.web.service.venta;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.web.config.MercadoPagoConfiguration;
import com.example.web.dto.venta.VentaItemRequest;
import com.example.web.models.Producto.Producto;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;

@Service
public class MercadoPagoService {

    private static final Logger logger = LoggerFactory.getLogger(MercadoPagoService.class);
    private final MercadoPagoConfiguration mpConfig;

    public MercadoPagoService(MercadoPagoConfiguration mpConfig) {
        this.mpConfig = mpConfig;
    }

    /**
     * Crea una preferencia de pago en Mercado Pago
     */
    public Preference crearPreferencia(
            List<VentaItemRequest> items,
            List<Producto> productos,
            String externalReference) throws MPException, MPApiException {

        try {
            logger.info("Creando preferencia de pago para {} items", items.size());

            List<PreferenceItemRequest> mpItems = new ArrayList<>();

            for (int i = 0; i < items.size(); i++) {
                VentaItemRequest itemReq = items.get(i);
                Producto producto = productos.get(i);

                logger.debug("Item {}: {} - Cantidad: {} - Precio: {}",
                        i, producto.getNombre(), itemReq.getCantidad(), producto.getPrecio());

                PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                        .id(producto.getId().toString())
                        .title(producto.getNombre())
                        .description(producto.getDescripcion())
                        .pictureUrl(producto.getImagenUrl())
                        .categoryId("others")
                        .quantity(itemReq.getCantidad())
                        .currencyId("PEN")
                        .unitPrice(producto.getPrecio())
                        .build();

                mpItems.add(itemRequest);
            }

            // URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(mpConfig.getSuccessUrl())
                    .failure(mpConfig.getFailureUrl())
                    .pending(mpConfig.getPendingUrl())
                    .build();

            logger.info("URLs configuradas - Success: {}, Failure: {}, Pending: {}, Notification: {}",
                    mpConfig.getSuccessUrl(),
                    mpConfig.getFailureUrl(),
                    mpConfig.getPendingUrl(),
                    mpConfig.getNotificationUrl());

            // Construir la preferencia
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(mpItems)
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .externalReference(externalReference)
                    .notificationUrl(mpConfig.getNotificationUrl())
                    .build();

            logger.info("Enviando request a Mercado Pago...");

            // Crear la preferencia en Mercado Pago
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            logger.info("Preferencia creada exitosamente - ID: {}, InitPoint: {}",
                    preference.getId(), preference.getInitPoint());

            return preference;

        } catch (MPApiException e) {
            logger.error("Error de API de Mercado Pago:");
            logger.error("  - Status Code: {}", e.getStatusCode());
            logger.error("  - Message: {}", e.getMessage());
            if (e.getApiResponse() != null) {
                logger.error("  - API Response: {}", e.getApiResponse().getContent());
            }
            logger.error("  - Cause: {}", e.getCause() != null ? e.getCause().getMessage() : "N/A");
            throw e;
        } catch (MPException e) {
            logger.error("Error de comunicación con Mercado Pago: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Obtiene información de un pago por su ID
     */
    public Payment obtenerPago(Long paymentId) throws MPException, MPApiException {
        try {
            logger.info("Obteniendo información del pago ID: {}", paymentId);
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(paymentId);
            logger.info("Pago obtenido - Status: {}, Amount: {}", payment.getStatus(), payment.getTransactionAmount());
            return payment;
        } catch (MPApiException e) {
            logger.error("Error al obtener pago {}: Status {}, Message: {}",
                    paymentId, e.getStatusCode(), e.getMessage());
            throw e;
        }
    }
}
