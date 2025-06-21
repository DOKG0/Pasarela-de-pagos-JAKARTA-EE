package org.tallerjava.servicioExterno.interfase.remota;

import org.tallerjava.servicioExterno.ServicioExternoMedioDePago;
import org.tallerjava.servicioExterno.datatypes.DTONotificacionTransferencia;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Tag(name="API del Servicio Externo")
@ApplicationScoped
@Path("/servicio-externo")
public class ServicioExternoAPI {
    @Inject
    ClienteHttpServicioExterno clienteHttpTransferencia; // Cliente REST

    @Inject
    ServicioExternoMedioDePago servicioExterno;

    @POST
    @Path("/notificar-transferencia")
    public Response notificarTransferencia(DTONotificacionTransferencia dto) {
        boolean resultado = servicioExterno.procesarPago(dto.getNroCuentaBancoComercio(), dto.getMonto(), dto.getIdComercio());
        System.out.println("[ServicioExterno] Resultado booleano del servicioExterno: " + resultado);

        if(resultado ) { 
                //clienteHttpTransferencia.enviarNotificacion(dto);
                return Response.ok(resultado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Fallo al procesar pago").build();
            }
    }
}
