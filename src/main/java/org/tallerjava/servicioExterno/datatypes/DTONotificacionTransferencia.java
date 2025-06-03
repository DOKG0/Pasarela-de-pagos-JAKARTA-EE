package org.tallerjava.servicioExterno.datatypes;

import lombok.Data;
@Data
public class DTONotificacionTransferencia {
    private String nroCuentaBancoComercio;
    private Integer idComercio;
    private double monto;
    private String codigoTransaccion;
}
