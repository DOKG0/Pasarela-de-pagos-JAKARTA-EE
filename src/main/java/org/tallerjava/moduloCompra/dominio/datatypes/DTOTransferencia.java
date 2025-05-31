package org.tallerjava.moduloCompra.dominio.datatypes;

import lombok.Data;
@Data
public class DTOTransferencia {
    private String nroCuentaBancoComercio;
    private Integer idComercio;
    private double monto;
    private DTOPago dtoPago;

    public DTOTransferencia buildDTONotificacionTransferencia() {
        DTOTransferencia notificacion = new DTOTransferencia();
        
        notificacion.setNroCuentaBancoComercio(this.nroCuentaBancoComercio);
        notificacion.setIdComercio(this.idComercio);
        notificacion.setMonto(this.monto);
        
        return notificacion;
    }
}
