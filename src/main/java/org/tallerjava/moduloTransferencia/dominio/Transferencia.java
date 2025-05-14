package org.tallerjava.moduloTransferencia.dominio;

import org.tallerjava.moduloComercio.dominio.Comercio;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transferencia {
    private Integer id;
    private Comercio comercio;
    private double monto;
    private double comision;
    private LocalDateTime fecha;
    private EstadoTransferencia estado;
    private String codigoAutorizacion;
    
}