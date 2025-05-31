package org.tallerjava.moduloTransferencia.interfase.evento.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventoTransferenciaRecibida {
    private String codigoTransaccion;
    private Integer idComercio;
    private BigDecimal monto;
    private LocalDateTime fecha;
}
