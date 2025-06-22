package org.tallerjava.moduloCompra.interfase.remota;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.tallerjava.moduloCompra.aplicacion.ServicioCompra;
import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOTransferencia;
import org.tallerjava.moduloCompra.infraestructura.seguridad.interceptors.ApiInterceptorCredencialesComercio;

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

@Tag(name="API del Módulo Compra")
@ApplicationScoped
@Path("/compra")
public class CompraAPI {
    
    @Inject
    ServicioCompra servicioCompra;

    @Inject
    ClienteHttpCompra httpClient;

    private static final Logger LOG = Logger.getLogger(CompraAPI.class.getName());

    
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
            boolean resultado = httpClient.enviarSolicitudPago(
                datosCompra
            );

            LOG.info("[Compra] Resultado booleano del Servicio Externo: " + resultado);

            //Se hace la logica interna del modulo y se le pasa el valor del servicio externo asi prevee que hacer con la compra creada
            servicioCompra.procesarPago(datosCompra.getIdComercio(), datosCompra.getMonto(), resultado, datosCompra.getIdPos());
            
            if (resultado) {
                return Response
                .ok()
                .build();
        } else {
            return Response
                .serverError()
                .entity("{\"error\": \"El pago fue rechazado\"}")
                .status(500)
                .build();
        }
    }

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
                        .status(500)
                        .build();
                }
            }

            DTOResumenVentas resumen = servicioCompra.resumenVentasPorPeriodo(idComercio, fechaInicio, fechaFin);

            if (resumen == null) {
                return Response
                    .serverError()
                    .entity("{\"error\": \"Error al generar el resumen\"}")
                    .status(500)
                    .build();
            } else {
                return Response.ok(resumen).build();
            }
    }

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
                .status(500)
                .build();
        } else {
            return Response.ok(resumen).build();
        }
    }
}
