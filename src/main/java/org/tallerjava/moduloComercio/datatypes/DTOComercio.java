package org.tallerjava.moduloComercio.datatypes;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.CuentaBancoComercio;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
public class DTOComercio {
    @Schema(hidden = true)
    private Integer id;

    @Schema(name = "nombre", example = "Mi Comercio")
    private String nombre;
    @Schema(name = "rut", example = "102030405060")
    private String rut;
    @Schema(name = "direccion", example = "18 de Julio 123")
    private String direccion;
    @Schema(name = "usuario", example = "miusuario123", requiredMode = RequiredMode.REQUIRED)
    private String usuario;
    @Schema(name = "nroCuentaBanco", example = "1234123412", requiredMode = RequiredMode.REQUIRED)
    private String nroCuentaBanco;
    @Schema(name = "password", example = "mipw123", requiredMode = RequiredMode.REQUIRED)
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
