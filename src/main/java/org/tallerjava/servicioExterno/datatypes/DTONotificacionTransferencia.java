package org.tallerjava.servicioExterno.datatypes;

import org.tallerjava.moduloCompra.dominio.datatypes.DTOPago;

import lombok.Data;
@Data
public class DTONotificacionTransferencia {
    private String nroCuentaBancoComercio;
    private Integer idComercio;
    private double monto;
    private DTOPago dtoPago;
    private String codigoTransaccion;

}
