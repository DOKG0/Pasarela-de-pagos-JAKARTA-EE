package org.tallerjava.moduloTransferencia.interfase.evento.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventoDepositoFinalizado {
    private Integer idDeposito;
    private Integer idComercio;
    private BigDecimal montoNeto;
    private BigDecimal comision;
    private LocalDateTime fecha;
    
}