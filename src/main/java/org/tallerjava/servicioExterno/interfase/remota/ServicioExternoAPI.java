package org.tallerjava.servicioExterno.interfase.remota;

import java.util.logging.Logger;

import org.tallerjava.servicioExterno.ServicioExternoMedioDePago;
import org.tallerjava.servicioExterno.datatypes.DTONotificacionTransferenciaServicioExterno;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

//Swagger
@Tag(name="API del Servicio Externo")
@Server(url="http://localhost:8080/TallerJakartaEEPasarelaPagos/api")
//Logica
@ApplicationScoped
@Path("/servicio-externo")
public class ServicioExternoAPI {

    private static final Logger LOG = Logger.getLogger(ServicioExternoAPI.class.getName());

    @Inject
    ClienteHttpServicioExterno clienteHttpTransferencia; // Cliente REST

    @Inject
    ServicioExternoMedioDePago servicioExterno;

    //Swagger
    @Operation(
        summary="Simula la acción de un servicio de medio de pago externo que puede aceptar o rechazar un pago")  
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "El pago fue aprobado (true) o rechazado (false)"),
        @ApiResponse(responseCode = "500", description = "Error del servidor")})
    @RequestBody(
        description = "Estructura del request",
        required = true,
        content = @Content(schema = @Schema(implementation = DTONotificacionTransferenciaServicioExterno.class)))
    //Logica
    @POST
    @Path("/notificar-transferencia")
    public Response notificarTransferencia(DTONotificacionTransferenciaServicioExterno dto) {
        LOG.info("[ServicioExterno] El servicio externo recibió los datos de pago:\n" + dto.toString());

        boolean resultado = servicioExterno.procesarPago(dto.getNroCuentaBancoComercio(), dto.getMonto(), dto.getIdComercio());

        LOG.info("[ServicioExterno] Resultado booleano del servicioExterno: " + resultado);

        return Response.ok(resultado).build();
    }
}
