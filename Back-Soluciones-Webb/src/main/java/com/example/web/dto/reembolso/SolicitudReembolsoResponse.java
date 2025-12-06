package com.example.web.dto.reembolso;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudReembolsoResponse {
    private Long id;
    private Long idVenta;
    private String nombreCliente;
    private String emailCliente;
    private BigDecimal totalVenta;
    private String motivo;
    private String estado;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaProcesamiento;
    private String nombreEmpleadoProceso;
    private String comentarioEmpleado;
}
