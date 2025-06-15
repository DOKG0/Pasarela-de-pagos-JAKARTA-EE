package org.tallerjava.moduloTransferencia.datatypes;

import lombok.Data;
@Data
public class DTONotificacionTransferencia {
    private String nroCuentaBancoComercio;
    private Integer idComercio;
    private double monto;
    private String codigoTransaccion;

    public DTONotificacionTransferencia buildDTONotificacionTransferencia() {
        DTONotificacionTransferencia notificacion = new DTONotificacionTransferencia();
        
        notificacion.setNroCuentaBancoComercio(this.nroCuentaBancoComercio);
        notificacion.setIdComercio(this.idComercio);
        notificacion.setMonto(this.monto);
        notificacion.setCodigoTransaccion(this.codigoTransaccion);
        
        return notificacion;
    }
}
