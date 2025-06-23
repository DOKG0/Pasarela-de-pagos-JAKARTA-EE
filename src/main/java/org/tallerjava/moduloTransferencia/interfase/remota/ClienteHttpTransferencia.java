package org.tallerjava.moduloTransferencia.interfase.remota;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ClienteHttpTransferencia {
    public boolean notificarBancoSoapHttp(String numeroCuenta, double monto, String codigoTransaccion) {
        try {
            String soapEndpointUrl = "http://localhost:8080/TallerJakartaEEPasarelaPagos/NotificacionBancoService";
            String soapBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.soap.remota.interfase.servicioExterno.tallerjava.org/\">"
                    +
                    "   <soapenv:Header/>" +
                    "   <soapenv:Body>" +
                    "      <ws:notificarSaldoEntrante>" +
                    "         <numeroCuenta>" + numeroCuenta + "</numeroCuenta>" +
                    "         <monto>" + monto + "</monto>" +
                    "         <codigoTransaccion>" + codigoTransaccion + "</codigoTransaccion>" +
                    "      </ws:notificarSaldoEntrante>" +
                    "   </soapenv:Body>" +
                    "</soapenv:Envelope>";

            Client client = ClientBuilder.newClient();
            Response response = client
                    .target(soapEndpointUrl)
                    .request(MediaType.TEXT_XML)
                    .post(Entity.entity(soapBody, MediaType.TEXT_XML));

            String responseStr = response.readEntity(String.class);
            return response.getStatus() == 200 && responseStr.contains(">ok<");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
