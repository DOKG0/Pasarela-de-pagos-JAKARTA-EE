package org.tallerjava.moduloTransferencia.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="deposito_MOD_TRANS")
public class Deposito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer comercioId; 
    
    private BigDecimal monto;
    private BigDecimal comision;
    private BigDecimal montoNeto;

    private String fecha;
    private Integer referenciaTransferencia;
    
    public Deposito(Integer comercioId, BigDecimal monto, Integer referencia) {
        this.comercioId = comercioId;
        this.monto = monto;
        this.fecha = LocalDate.now().toString();
        this.comision = calcularComision(monto);
        this.referenciaTransferencia = referencia;
        this.montoNeto = monto.subtract(this.comision);
    }
    
    private BigDecimal calcularComision(BigDecimal monto) {
        BigDecimal porcentajeComision = new BigDecimal("0.05"); // 5%
        return monto.multiply(porcentajeComision);
    }
}