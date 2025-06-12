package org.tallerjava.moduloCompra.dominio.datatypes;

import lombok.Data;
@Data
public class DTOTransferencia {
    private String nroCuentaBancoComercio;
    private Integer idPos;
    private Integer idComercio;
    private double monto;
    private DTOPago dtoPago;
}
