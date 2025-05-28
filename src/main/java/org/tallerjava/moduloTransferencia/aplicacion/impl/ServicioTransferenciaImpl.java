package org.tallerjava.moduloTransferencia.aplicacion.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloTransferencia.dominio.Deposito;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;


public class ServicioTransferenciaImpl {
    public Boolean recibirNotificacionTransferenciaDesdeMedioPago(Deposito datosTransferencia){
        return false;
    }
    public List<Deposito> consultarDepositos(Comercio comercio, LocalDate fechaInicial, LocalDate fechaFin){
        return new ArrayList<>();
    }

    public boolean notificarBancoConHttp(String numeroCuenta, double monto, String codigoTransaccion) {
        try {
            String soapEndpointUrl = "http://localhost:8080/TallerJakartaEEPasarelaPagos/NotificacionBancoService";
            String soapAction = "";

            String soapBody =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://soap.ws.interfase.moduloComercio.tallerjava.org/\">" +
                "   <soapenv:Header/>" +
                "   <soapenv:Body>" +
                "      <ws:notificarSaldoEntrante>" +
                "         <numeroCuenta>" + numeroCuenta + "</numeroCuenta>" +
                "         <monto>" + monto + "</monto>" +
                "         <codigoTransaccion>" + codigoTransaccion + "</codigoTransaccion>" +
                "      </ws:notificarSaldoEntrante>" +
                "   </soapenv:Body>" +
                "</soapenv:Envelope>";

            URL url = new URL(soapEndpointUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            conn.setRequestProperty("SOAPAction", soapAction);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(soapBody.getBytes());
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // Leer la respuesta y buscar "ok"
                try (InputStream is = conn.getInputStream()) {
                    String response = new String(is.readAllBytes());
                    return response.contains(">ok<");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
