package org.tallerjava.moduloCompra.dominio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comercio {
    private Integer id;
    private String nombre;
    private String rut;
    private String direccion;
    
}