package com.example.web.models.venta;

import java.time.LocalDateTime;

import com.example.web.models.auth.Cliente;
import com.example.web.models.auth.Empleado;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "solicitud_reembolso")
public class SolicitudReembolso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(length = 500)
    private String motivo;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_procesamiento")
    private LocalDateTime fechaProcesamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procesado_por_id")
    private Empleado procesadoPor;

    @Column(name = "comentario_empleado", length = 500)
    private String comentarioEmpleado;
}
