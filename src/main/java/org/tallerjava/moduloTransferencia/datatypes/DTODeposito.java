package org.tallerjava.moduloTransferencia.datatypes;

import java.math.BigDecimal;

import org.tallerjava.moduloTransferencia.dominio.Deposito;

import lombok.Data;
@Data
public class DTODeposito {
    private Integer id;
    private Integer idComercio;
    private BigDecimal monto;
    private BigDecimal comision;
    private BigDecimal montoNeto;
    private String fecha;
    private Integer referenciaTransferencia;

    public Deposito buildDeposito() {
        Deposito deposito = new Deposito();
        
        deposito.setId(this.id);
        deposito.setComercioId(this.idComercio);
        deposito.setMonto(this.monto);
        deposito.setComision(this.comision);
        deposito.setMontoNeto(this.montoNeto);
        deposito.setFecha(this.fecha);
        deposito.setReferenciaTransferencia(this.referenciaTransferencia);
        
        return deposito;
    }
}
