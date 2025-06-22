package org.tallerjava.moduloTransferencia.interfase.remota;

import java.time.LocalDate;
import java.util.List;

import org.tallerjava.moduloTransferencia.aplicacion.ServicioTransferencia;
import org.tallerjava.moduloTransferencia.datatypes.DTONotificacionTransferencia;
import org.tallerjava.moduloTransferencia.dominio.Deposito;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.servers.Server;
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

//Swagger
@Tag(name="API del Módulo Transferencia")
@Server(url="http://localhost:8080/TallerJakartaEEPasarelaPagos/api")
//Logica
@ApplicationScoped
@Path("/transferencia")
public class TransferenciaAPI {
    
    @Inject
    ServicioTransferencia servicioTransferencia;

    //Swagger
    @Operation(
        summary="Notificación de una transferencia en la pasarela de pago",
        description="Recibe una notificacion de un servicio externo cuando una nueva transferencia es realizada a la cuenta bancaria de la pasarela. Con esta información se agrega una nueva transferencia y un nuevo depósito a la cuenta bancaria de la pasarela y se notifica al banco.")    
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "La transferencia se registró exitosamente"),
        @ApiResponse(responseCode = "500", description = "Ocurrió un error al registrar la transferencia o el depósito")})
    @RequestBody(
        description = "Estructura del request",
        required = true,
        content = @Content(schema = @Schema(implementation = DTONotificacionTransferencia.class)))
    //Logica
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
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    //Swagger
    @Operation(
        summary="Permite a un comercio consultar los depósitos de su cuenta bancaria en un determinado período",
        description="Los comercios pueden consultar los depósitos que han sido realizados en su cuenta bancaria dentro de un determinado período de tiempo especificando una fecha de inicio y una fecha de fin.",
        parameters = {
            @Parameter(
                name = "fechaInicial", 
                example = "2025-05-18", 
                required = true, 
                description = "Fecha de inicio del período"),
            @Parameter(
                name = "fechaFinal", 
                example = "2025-07-10", 
                required = true, 
                description = "Fecha de fin del período"),
            @Parameter(
                name = "idComercio",
                example = "1",
                required = true,
                description = "Id del comercio para el cual se quiere consultar los depósitos")})    
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "Los depósitos se obtuvieron exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los parámetros proveídos no son válidos"),
        @ApiResponse(responseCode = "404", description = "No se encontró un comercio con el id provisionado"),
        @ApiResponse(responseCode = "500", description = "Ocurrió un error al consultar los depósitos")})
    //Logica
    @GET
    @Path("/depositos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarDepositos(
            @QueryParam("idComercio") Integer idComercio,
            @QueryParam("fechaInicial") String fechaInicialStr,
            @QueryParam("fechaFinal") String fechaFinalStr) {

        if (idComercio == null || fechaInicialStr == null || fechaFinalStr == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Todos los parámetros son requeridos\"}")
                    .build();
        }

        LocalDate fechaInicial = null, fechaFinal = null;
        try {
            fechaInicial = LocalDate.parse(fechaInicialStr);
            fechaFinal = LocalDate.parse(fechaFinalStr);
        } catch (Exception e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Las fechas ingresadas no son válidas\"}")
                    .build();
        }

        try {
            List<Deposito> depositos = servicioTransferencia.consultarDepositos(
                    idComercio,
                    fechaInicial,
                    fechaFinal);

            if (depositos == null) {
                return Response
                    .serverError()
                    .entity("{\"error\": \"No se encontró un comercio con el id provisionado\"}")
                    .status(Response.Status.NOT_FOUND)
                    .build();
            }

            return Response
                    .ok(depositos, MediaType.APPLICATION_JSON)
                    .build();

        } catch (Exception e) {
            return Response
                    .serverError()
                    .entity("{\"error\": \"Error al consultar depósitos: " + e.getMessage() + "\"}")
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
