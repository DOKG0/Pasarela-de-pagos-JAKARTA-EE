package org.tallerjava.moduloComercio.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.tallerjava.moduloComercio.dominio.CuentaBancoComercio;

public class CuentaBancoComercioTest {
    
    @Test
    void deberiaCrearCuentaConSaldoCero() {
        CuentaBancoComercio cuenta = new CuentaBancoComercio();
        cuenta.setNumeroCuenta("123456");
        cuenta.setBanco("BROU");
        
        assertEquals(BigDecimal.ZERO, cuenta.getSaldo());
    }

    @Test
    void deberiaValidarCuentaCorrectamente() {
        CuentaBancoComercio cuentaValida = new CuentaBancoComercio();
        cuentaValida.setNumeroCuenta("123456");
        cuentaValida.setBanco("BROU");
        
        assertTrue(cuentaValida.esCuentaValida());
        
        CuentaBancoComercio cuentaInvalida = new CuentaBancoComercio();
        assertFalse(cuentaInvalida.esCuentaValida());
    }

}
