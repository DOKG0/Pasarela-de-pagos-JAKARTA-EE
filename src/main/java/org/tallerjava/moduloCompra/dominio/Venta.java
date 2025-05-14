package org.tallerjava.moduloCompra.dominio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venta {
    private Integer id;
    private boolean exitoso;
    private String codigoAutorizacion;
    private String mensaje;
    
    public Venta(boolean exitoso, String codigoAutorizacion, String mensaje) {
        this.exitoso = exitoso;
        this.codigoAutorizacion = codigoAutorizacion;
        this.mensaje = mensaje;
    }
    
}