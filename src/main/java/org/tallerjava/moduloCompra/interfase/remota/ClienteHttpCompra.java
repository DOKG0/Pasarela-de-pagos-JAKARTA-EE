package org.tallerjava.moduloCompra.interfase.remota;

import org.tallerjava.moduloCompra.dominio.datatypes.DTOTransferencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ClienteHttpCompra {

    private static final String ENDPOINT_SERVICIO_EXTERNO = "http://localhost:8080/TallerJakartaEEPasarelaPagos/api/servicio-externo/notificar-transferencia";


    public boolean enviarSolicitudPago(DTOTransferencia dto) {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client
                .target(ENDPOINT_SERVICIO_EXTERNO)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(dto, MediaType.APPLICATION_JSON));

            /**
             * DTONotificacionTransferencia en el servicio externo es diferente a DTOTransferencia de este modulo
             * Esto genera un error en el lado del servicio externo al parsear el objeto, 
             * por lo que siempre devuelve un codigo 400
             */
            System.out.println("Response - ClienteHttpCompra: ");
            System.out.println(response.readEntity(String.class));
            System.out.println("Response status: " + response.getStatus());

            return response.getStatus() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}