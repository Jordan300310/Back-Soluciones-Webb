package com.example.web.dto.reembolso;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudReembolsoRequest {
    private Long idVenta;
    private String motivo;
}
