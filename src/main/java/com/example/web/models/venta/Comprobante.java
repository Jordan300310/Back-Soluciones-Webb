package com.example.web.models.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comprobante")
public class Comprobante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String serie;
    @Column(nullable = false, length = 20)
    private String numero;
    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;
    @Column(nullable = false, length = 3)
    private String moneda;
    @Column(name = "ruc_emisor", nullable = false, length = 11)
    private String rucEmisor;
    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;
    @Column(precision = 10, scale = 2)
    private BigDecimal igv;
    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @OneToOne(optional = false)
    @JoinColumn(name = "venta_id", referencedColumnName = "id")
    private Venta venta;

}
