package org.tallerjava.moduloComercio.dominio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuentaBancoComercio {
    private Integer id;
    private String numeroCuenta;
    private String banco;
    private String tipoCuenta; // CORRIENTE o AHORRO
    private String moneda; // USD o UYU supongo
    private Comercio comercio;

    // Métodos de negocio
    public boolean esCuentaValida() {
        return numeroCuenta != null && !numeroCuenta.isEmpty() &&
               banco != null && !banco.isEmpty();
    }
}