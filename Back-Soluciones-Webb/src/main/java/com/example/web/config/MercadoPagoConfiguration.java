package com.example.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.mercadopago.MercadoPagoConfig;

import jakarta.annotation.PostConstruct;

@Configuration
public class MercadoPagoConfiguration {

    @Value("${mercadopago.access.token:}")
    private String accessToken;

    @Value("${mercadopago.success.url:http://localhost:4200/checkout/success}")
    private String successUrl;

    @Value("${mercadopago.failure.url:http://localhost:4200/checkout/failure}")
    private String failureUrl;

    @Value("${mercadopago.pending.url:http://localhost:4200/checkout/pending}")
    private String pendingUrl;

    @Value("${mercadopago.notification.url:}")
    private String notificationUrl;

    @PostConstruct
    public void init() {
        if (accessToken != null && !accessToken.isEmpty()) {
            MercadoPagoConfig.setAccessToken(accessToken);
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public String getFailureUrl() {
        return failureUrl;
    }

    public String getPendingUrl() {
        return pendingUrl;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }
}
