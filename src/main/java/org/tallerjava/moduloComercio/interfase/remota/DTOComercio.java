package org.tallerjava.moduloComercio.interfase.remota;

import org.tallerjava.moduloComercio.dominio.Comercio;

import lombok.Data;

@Data
public class DTOComercio {
    private Integer id;
    private String nombre;
    private String rut;
    private String direccion;
    private String contraseña;

    public Comercio buildComercio() {
        Comercio comercio = new Comercio();
        comercio.setNombre(nombre);
        comercio.setDireccion(direccion);
        comercio.setRut(rut);
        comercio.setContraseña(contraseña);

        return comercio;
    }
}
