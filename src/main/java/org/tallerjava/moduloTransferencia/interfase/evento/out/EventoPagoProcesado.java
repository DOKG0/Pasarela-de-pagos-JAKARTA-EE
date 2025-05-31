package org.tallerjava.moduloTransferencia.interfase.evento.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventoPagoProcesado {
    private String codigoTransaccion;
    private Integer idComercio;
    private BigDecimal montoBruto;
    private BigDecimal montoNeto;
    private LocalDateTime fecha;
}
