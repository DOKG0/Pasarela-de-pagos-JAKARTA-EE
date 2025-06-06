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

            if (response.getStatus() == 200) {
                return response.readEntity(Boolean.class); 
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}