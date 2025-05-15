package org.tallerjava.moduloCompra.dominio;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumenVentas {
    private Comercio comercio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cantidadVentas;
    private double montoTotal;
    
    public ResumenVentas(Comercio comercio, LocalDate fechaInicio, LocalDate fechaFin) {
        this.comercio = comercio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    public void agregarVenta(double monto) {
        this.cantidadVentas++;
        this.montoTotal += monto;
    }
}