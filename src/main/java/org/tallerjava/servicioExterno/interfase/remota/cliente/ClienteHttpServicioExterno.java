package org.tallerjava.servicioExterno.interfase.remota.cliente;

import org.tallerjava.servicioExterno.datatypes.DTONotificacionTransferenciaServicioExterno;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ClienteHttpServicioExterno {

    private static final String ENDPOINT_TRANSFERENCIA = "http://localhost:8080/TallerJakartaEEPasarelaPagos/api/transferencia/notificacion";
    private static final String ENDPOINT_COMPRA = "http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/notificacion";

    public boolean enviarNotificacion(DTONotificacionTransferenciaServicioExterno dto) {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client
                    .target(ENDPOINT_TRANSFERENCIA)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(dto, MediaType.APPLICATION_JSON));

            return response.getStatus() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean enviarRespuestaProcesoPago(boolean exito) {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client
                .target(ENDPOINT_COMPRA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(exito, MediaType.APPLICATION_JSON));

            return response.getStatus() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}