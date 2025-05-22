package org.tallerjava.moduloTransferencia.interfase.remota;

import java.time.LocalDate;
import java.util.List;

import org.tallerjava.moduloTransferencia.aplicacion.ServicioTransferencia;
import org.tallerjava.moduloTransferencia.datatypes.DTONotificacionTransferencia;
import org.tallerjava.moduloTransferencia.dominio.Deposito;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name="API del Módulo Transferencia")
@ApplicationScoped
@Path("/transferencia")
public class TransferenciaAPI {
    
    @Inject
    ServicioTransferencia servicioTransferencia;

    // curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/transferencia/notificacion -H "Content-Type: application/json" -d '{"nroCuentaBancoComercio":"123456789","monto":1000.50,"codigoTransaccion":"TX-123456","idComercio":1}'
    @POST
    @Path("/notificacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response recibirNotificacionTransferencia(DTONotificacionTransferencia datos) {
        boolean resultado = servicioTransferencia.recibirNotificacionTransferenciaDesdeMedioPago(
                datos.getNroCuentaBancoComercio(),
                datos.getMonto(),
                datos.getCodigoTransaccion(),
                datos.getIdComercio());

        if (resultado) {
            return Response
                    .ok()
                    .build();
        } else {
            return Response
                    .serverError()
                    .entity("{\"error\": \"Error al procesar la transferencia\"}")
                    .status(500)
                    .build();
        }
    }

    // curl -v "http://localhost:8080/TallerJakartaEEPasarelaPagos/api/transferencia/depositos?idComercio=1&fechaInicial=2023-01-01&fechaFinal=2023-12-31"
    @GET
    @Path("/depositos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarDepositos(
            @QueryParam("idComercio") Integer idComercio,
            @QueryParam("fechaInicial") String fechaInicial,
            @QueryParam("fechaFinal") String fechaFinal) {

        if (idComercio == null || fechaInicial == null || fechaFinal == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Todos los parámetros son requeridos\"}")
                    .build();
        }

        try {
            List<Deposito> depositos = servicioTransferencia.consultarDepositos(
                    idComercio,
                    LocalDate.parse(fechaInicial),
                    LocalDate.parse(fechaFinal));

            return Response
                    .ok(depositos, MediaType.APPLICATION_JSON)
                    .build();

        } catch (Exception e) {
            return Response
                    .serverError()
                    .entity("{\"error\": \"Error al consultar depósitos: " + e.getMessage() + "\"}")
                    .status(500)
                    .build();
        }
    }
}
