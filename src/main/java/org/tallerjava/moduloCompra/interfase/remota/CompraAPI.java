package org.tallerjava.moduloCompra.interfase.remota;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.tallerjava.moduloCompra.aplicacion.ServicioCompra;
import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.Tarjeta;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOPago;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name="API del Módulo Compra")
@ApplicationScoped
@Path("/compra")
public class CompraAPI {
    
    @Inject
    ServicioCompra servicioCompra;

    //curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/1/nueva-compra -H "Content-Type: application/json" -d '{"nroTarjeta": "123", "marcaTarjeta": "visa", "fechaVtoTarjeta": "2025-05-17", "importe": 10000 }'
    @POST
    @Path("/{idComercio}/nueva-compra")
    @Produces(MediaType.APPLICATION_JSON)
    public Response procesarPago(
        @PathParam("idComercio") Integer idComercio, 
        DTOPago datosCompra) {
            
            LocalDate fechaVtoTarjeta = null;
            try {
                fechaVtoTarjeta = LocalDate.parse(datosCompra.getFechaVtoTarjeta());
            } catch (Exception e) {
                return Response
                .serverError()
                .entity("{\"error\": \"Fecha de vencimiento de tarjeta invalida\"}")
                .status(500)
                .build();
            }

            Tarjeta tarjeta = new Tarjeta(
                datosCompra.getNroTarjeta(),
                datosCompra.getMarcaTarjeta(),
                fechaVtoTarjeta
            );

            boolean resultado = servicioCompra.procesarPago(
                idComercio, 
                datosCompra.getImporte(), 
                tarjeta);

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

    //curl -v "http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/1/resumen/periodo?fechaInicio=2025-05-18&fechaFin=2025-05-18"
    @GET
    @Path("/{idComercio}/resumen/periodo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerResumenDeVentasPorPeriodo(
        @PathParam("idComercio") Integer idComercio,
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

    //curl -v "http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/1/resumen/por-estado?estado=APROBADA"
    //curl -v "http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/1/resumen/por-estado?estado=RECHAZADA"
    @GET
    @Path("/{idComercio}/resumen/por-estado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerResumenDeVentasDiario(
        @PathParam("idComercio") Integer idComercio,
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
