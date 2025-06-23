package org.tallerjava.moduloCompra.interfase.remota;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.tallerjava.moduloCompra.aplicacion.ServicioCompra;
import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOTransferencia;
import org.tallerjava.moduloCompra.infraestructura.seguridad.interceptors.ApiInterceptorCredencialesComercio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

//Swagger
@Tag(name="API del Módulo Compra")
@Server(url="http://localhost:8080/TallerJakartaEEPasarelaPagos/api")
@SecurityScheme(
    name = "Basic",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER,
    paramName = "Authorization",
    scheme = "Basic"
)
//Logica
@ApplicationScoped
@Path("/compra")
public class CompraAPI {
    
    @Inject
    ServicioCompra servicioCompra;

    @Inject
    ClienteHttpCompra httpClient;

    private static final Logger LOG = Logger.getLogger(CompraAPI.class.getName());

    //Swagger
    @Operation(
        summary="Envía la solicitud de pago para realizar una nueva compra",
        description="Un comercio autenticado envía los datos de la compra para que el servidor procese los datos, los envíe al servicio externo de pago y dé de alta una nueva compra. El estado de la compra dependerá del resultado devuelto por el servicio externo. Un pago puede ser rechazado por el propio servicio de la pasarela de pagos si los datos provisionados no son válidos, si el pos que se utiliza no está habilitado o si ocurrió un error al procesar la solicitud.",
        security = @SecurityRequirement(name = "Basic"))
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "El pago fue aceptado"),
        @ApiResponse(responseCode = "403", description = "Fallo por falta de credenciales o credenciales incorrectas"),
        @ApiResponse(responseCode = "500", description = "El pago fue rechazado")})
    @RequestBody(
        description = "Estructura del request",
        required = true,
        content = @Content(schema = @Schema(implementation = DTOTransferencia.class)))
    //Logica
    @POST
    @Path("/{idComercio}/nueva-compra")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("comercio")
    @ApiInterceptorCredencialesComercio
    public Response procesarPago(
        @PathParam("idComercio") Integer idComercio,
        @Context SecurityContext securityContext,
        DTOTransferencia datosCompra) {  
            
        // El httpClient envia la solicitud al servicio externo, el servicio devuelve true or false segun el calculo.
        boolean resultado = httpClient.enviarSolicitudPago(datosCompra);

        LOG.info("[Compra] Resultado booleano del Servicio Externo: " + resultado);

        //Se hace la logica interna del modulo y se le pasa el valor del servicio externo asi prevee que hacer con la compra creada
        boolean nuevaCompraRegistrada = servicioCompra.ingresarNuevaCompra(
            datosCompra.getIdComercio(), 
            datosCompra.getMonto(), 
            resultado, 
            datosCompra.getIdPos());
        
        if (!nuevaCompraRegistrada) {
            return Response
                .serverError()
                .entity("{\"error\": \"El pago fue rechazado por la pasarela de pagos. Verifique los datos del comercio\"}")
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        }

        if (resultado) {
            return Response
            .ok()
            .entity("{\"ok\": \"El pago fue aceptado\"}")
            .build();
        } else {
            return Response
                .serverError()
                .entity("{\"error\": \"El pago fue rechazado por el servicio externo de medio de pago\"}")
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        }
    }

    //Swagger
    @Operation(
        summary="Solicita un resumen de ventas con las compras realizadas en un determinado período",
        description="Un comercio autenticado puede solicitar un resumen con las ventas realizadas en un determinado período de tiempo si provee una fecha de inicio y una fecha de fin.",
        parameters = {
            @Parameter(
                name = "fechaInicio", 
                example = "2025-05-18", 
                required = true, 
                description = "Fecha de inicio del período"),
            @Parameter(
                name = "fechaFin", 
                example = "2025-07-10", 
                required = true, 
                description = "Fecha de fin del período")
        })
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "Se recuperó exitosamente el resumen de ventas"),
        @ApiResponse(responseCode = "400", description = "No se pudo procesar los parámetros provisionados"),
        @ApiResponse(responseCode = "403", description = "Fallo por falta de credenciales o credenciales incorrectas"),
        @ApiResponse(responseCode = "404", description = "El comercio con el id provisionado no existe"),
        @ApiResponse(responseCode = "500", description = "Ocurrió un error en el servidor y no se logró generar el resumen")})
    //Logica
    @GET
    @Path("/{idComercio}/resumen/periodo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("comercio")
    @ApiInterceptorCredencialesComercio
    public Response obtenerResumenDeVentasPorPeriodo(
        @PathParam("idComercio") Integer idComercio,
        @Context SecurityContext securityContext,
        @QueryParam("fechaInicio") String fechaInicioStr,
        @QueryParam("fechaFin") String fechaFinStr) {
            /*
                Se espera que la API reciba en el body las fechas en formato string por QueryParam
                Se espera que siga el formato de LocalDate 'YYYY-MM-DD', que luego es convertido a LocalDateTime
                Se busca facilitar la formacion del request
                Ademas, la hora y la zona no es relevante para el caso de uso particular, 
                pero si lo es al momento de guardar una compra
            */
            LocalDateTime fechaInicio = null, fechaFin = null;

            try {
                fechaInicio = LocalDate.parse(fechaInicioStr).atStartOfDay();
                fechaFin = LocalDate.parse(fechaFinStr).atTime(23,59,59);
            } finally {
                if (fechaInicio == null || fechaFin == null) {
                    return Response
                        .serverError()
                        .entity("{\"error\": \"Error al procesar los parametros\"}")
                        .status(Response.Status.BAD_REQUEST)
                        .build();
                }
            }

            DTOResumenVentas resumen = servicioCompra.resumenVentasPorPeriodo(idComercio, fechaInicio, fechaFin);

            if (resumen == null) {
                return Response
                    .serverError()
                    .entity("{\"error\": \"Error al generar el resumen\"}")
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
            } else {
                return Response.ok(resumen).build();
            }
    }

    //Swagger
    @Operation(
        summary="Solicita un resumen de ventas con las compras filtradas según su estado.",
        description="Un comercio autenticado puede solicitar un resumen con todas las ventas que tengan un determinado estado: PENDIENTE, APROBADA, RECHAZADA",
        parameters = {
            @Parameter(
                name = "estado", 
                example = "APROBADA", 
                required = true, 
                description = "Estado de la compra",
                content = @Content(schema = @Schema(implementation = EstadoCompra.class)))
        })
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "Se recuperó exitosamente el resumen de ventas"),
        @ApiResponse(responseCode = "403", description = "Fallo por falta de credenciales o credenciales incorrectas"),
        @ApiResponse(responseCode = "404", description = "El comercio con el id provisionado no existe"),
        @ApiResponse(responseCode = "500", description = "Ocurrió un error en el servidor y no se logró generar el resumen")})
    //Logica
    @GET
    @Path("/{idComercio}/resumen/por-estado")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("comercio")
    @ApiInterceptorCredencialesComercio
    public Response obtenerResumenDeVentasDiario(
        @PathParam("idComercio") Integer idComercio,
        @Context SecurityContext securityContext,
        @QueryParam("estado") EstadoCompra estado) {

        DTOResumenVentas resumen = servicioCompra.resumenVentasPorEstado(idComercio, estado);
        if (resumen == null) {
            return Response
                .serverError()
                .entity("{\"error\": \"Error al generar el resumen\"}")
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        } else {
            return Response.ok(resumen).build();
        }
    }
}
