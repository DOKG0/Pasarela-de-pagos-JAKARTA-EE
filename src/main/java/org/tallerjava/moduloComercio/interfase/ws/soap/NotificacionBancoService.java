package org.tallerjava.moduloComercio.interfase.ws.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService
public class NotificacionBancoService {

    @WebMethod
    public String notificarSaldoEntrante(
        @WebParam(name = "numeroCuenta") String numeroCuenta,
        @WebParam(name = "monto") double monto,
        @WebParam(name = "codigoTransaccion") String codigoTransaccion
    ){
        return "ok";
    }
}