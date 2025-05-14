package org.tallerjava.moduloTransferencia.dominio;

import org.tallerjava.moduloComercio.dominio.Comercio;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deposito {
    private Integer id;
    private Comercio comercio;
    private double monto;
    private LocalDate fecha;
    private String referencia;
    
    public Deposito(Comercio comercio, double monto, String referencia) {
        this.comercio = comercio;
        this.monto = monto;
        this.fecha = LocalDate.now();
        this.referencia = referencia;
    }
    
}