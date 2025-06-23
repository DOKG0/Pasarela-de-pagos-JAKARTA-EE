package org.tallerjava.moduloCompra.interfase.remota;

import java.util.logging.Logger;

import org.tallerjava.moduloCompra.dominio.datatypes.DTOTransferencia;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOTransferenciaOUT;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ClienteHttpCompra {

    private static final Logger LOG = Logger.getLogger(ClienteHttpCompra.class.getName());

    private static final String ENDPOINT_SERVICIO_EXTERNO = "http://localhost:8080/TallerJakartaEEPasarelaPagos/api/servicio-externo/notificar-transferencia";


    public boolean enviarSolicitudPago(DTOTransferencia dto) {
        try {
            Client client = ClientBuilder.newClient();

            DTOTransferenciaOUT dataTransferencia = new DTOTransferenciaOUT();
            dataTransferencia.setDtoPago(dto.getDtoPago());
            dataTransferencia.setIdComercio(dto.getIdComercio());
            dataTransferencia.setNroCuentaBancoComercio(dto.getNroCuentaBancoComercio());
            dataTransferencia.setMonto(dto.getMonto());

            LOG.info("[COMPRA][ClienteHttpCompra] Datos enviados al servicio externo:\n" 
                + dataTransferencia.toString());

            Response response = client
                .target(ENDPOINT_SERVICIO_EXTERNO)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(dataTransferencia, MediaType.APPLICATION_JSON));

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