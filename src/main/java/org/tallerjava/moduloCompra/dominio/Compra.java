package org.tallerjava.moduloCompra.dominio;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compra {
    private Integer id;
    private Comercio comercio;
    private double monto;
    private LocalDateTime fecha;
    private EstadoCompra estado;
    
    //private String codigoAutorizacion;
    
    public Compra(Comercio comercio, double monto) {
        this.comercio = comercio;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoCompra.PENDIENTE;
    }
    
    public void marcarComoAprobada() {
        this.estado = EstadoCompra.APROBADA;
        //this.codigoAutorizacion = codigoAutorizacion;
    }
    
    public void marcarComoRechazada() {
        this.estado = EstadoCompra.RECHAZADA;
    }
    
}