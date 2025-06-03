package org.tallerjava.moduloTransferencia.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.tallerjava.servicioExterno.interfase.remota.ClienteHttpTransferencia;

public class ServicioTransferenciaImplTest {

    @Test
    public void testNotificarBancoConHttp() {
        ClienteHttpTransferencia cliente = new ClienteHttpTransferencia();
        String numeroCuenta = "123456";
        double monto = 100.0;
        String codigoTransaccion = "test-ref";

        boolean resultado = cliente.notificarBancoSoapHttp(numeroCuenta, monto, codigoTransaccion);

        assertTrue(resultado, "La notificacion al banco via HTTP deberia devolver true si responde 'ok'");
    }
}