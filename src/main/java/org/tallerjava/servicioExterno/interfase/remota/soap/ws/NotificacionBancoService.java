package org.tallerjava.servicioExterno.interfase.remota.soap.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService
public class NotificacionBancoService {

    @WebMethod
    public String notificarSaldoEntrante(
        @WebParam(name = "numeroCuenta") String numeroCuenta,
        @WebParam(name = "monto") double monto,
        @WebParam(name = "codigoTransaccion") String codigoTransaccion) {

        System.out.println("[NotificacionBancoService] Saldo entrante notificado");
        return "ok";
    }
}