package org.tallerjava.moduloComercio.datatypes;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.CuentaBancoComercio;

import lombok.Data;

@Data
public class DTOComercio {
    private Integer id;
    private String nombre;
    private String rut;
    private String direccion;
    private String usuario;
    private String nroCuentaBanco;
    private String password;

    public Comercio buildComercio() {
        Comercio comercio = new Comercio();
        CuentaBancoComercio cuentaBancoComercio = new CuentaBancoComercio();
        
        cuentaBancoComercio.setNumeroCuenta(nroCuentaBanco);

        comercio.setNombre(nombre);
        comercio.setDireccion(direccion);
        comercio.setRut(rut);
        comercio.setUsuario(usuario);
        comercio.setCuentaBancoComercio(cuentaBancoComercio);

        return comercio;
    }
}
