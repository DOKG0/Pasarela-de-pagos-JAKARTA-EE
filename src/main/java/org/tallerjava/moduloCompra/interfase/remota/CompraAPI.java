package org.tallerjava.moduloCompra.interfase.remota;

import java.time.LocalDate;

import org.tallerjava.moduloCompra.aplicacion.ServicioCompra;
import org.tallerjava.moduloCompra.dominio.Tarjeta;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOPago;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
}
