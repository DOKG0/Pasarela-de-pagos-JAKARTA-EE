package org.tallerjava.moduloTransferencia.dominio;

import java.math.BigDecimal;
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
@Entity
@Table(name="transferencia_MOD_TRANS")
public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String idTransaccionMedioPago;
    private String nroCuentaBancoComercio;
    private Integer idComercio; 
    
    private BigDecimal monto;
    private BigDecimal comision;

    private EstadoTransferencia estado;
    
    private LocalDateTime fecha;

    public Transferencia(String idTransaccionMedioPago, BigDecimal monto, String nroCuenta, Integer idComercio) {
        this.idTransaccionMedioPago = idTransaccionMedioPago;
        this.idComercio = idComercio;
        this.monto = monto;
        this.nroCuentaBancoComercio = nroCuenta;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoTransferencia.PENDIENTE;
    }

    public void marcarComoCompletada() {
        this.estado = EstadoTransferencia.COMPLETADA;
    }
}