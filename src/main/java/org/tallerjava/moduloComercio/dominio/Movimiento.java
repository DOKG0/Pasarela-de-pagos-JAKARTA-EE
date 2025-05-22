package org.tallerjava.moduloComercio.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="movimiento_MOD_COMERCIO")
@Table(name="movimiento_MOD_COMERCIO")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String origen;
    private String referencia;
    private LocalDateTime fecha;

    private BigDecimal montoNeto;
    private BigDecimal comision;

    public Movimiento(String origen, String referencia, BigDecimal comision, BigDecimal montoNeto) {
        this.origen = origen;
        this.referencia = referencia;
        this.fecha = LocalDateTime.now();
        this.comision = comision;
        this.montoNeto = montoNeto;
    }
}
