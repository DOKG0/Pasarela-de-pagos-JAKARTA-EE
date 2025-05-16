package org.tallerjava.moduloComercio.interfase.remota;

import org.tallerjava.moduloComercio.dominio.Comercio;

import lombok.Data;

@Data
public class DTOModificacionComercio {
    private String nombre;
    private String rut;
    private String direccion;

    public Comercio buildComercio() {
        Comercio comercio = new Comercio();
        comercio.setNombre(nombre);
        comercio.setDireccion(direccion);
        comercio.setRut(rut);

        return comercio;
    }
}
