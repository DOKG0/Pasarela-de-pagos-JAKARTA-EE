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
    ClienteHttpTransferencia clienteHttpTransferencia; // Cliente REST

    @Inject
    ServicioExternoMedioDePago servicioExterno;

    @POST
    @Path("/notificar-transferencia")
    public Response notificarTransferencia(DTONotificacionTransferencia dto) {
        boolean resultado = servicioExterno.procesarPago(dto.getNroCuentaBancoComercio(), dto.getMonto(), dto.getIdComercio());
        if(resultado ) { 
            boolean exito = clienteHttpTransferencia.enviarNotificacion(dto);

            if (exito) {
                return Response.ok("Notificación enviada correctamente al módulo Transferencia").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Fallo al enviar notificación").build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Fallo al procesar pago").build();
    }
}
