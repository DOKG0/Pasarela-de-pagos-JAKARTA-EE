package org.tallerjava.moduloComercio.interfase.remota;

import org.tallerjava.moduloComercio.aplicacion.ServicioComercio;
import org.tallerjava.moduloComercio.datatypes.DTOComercio;
import org.tallerjava.moduloComercio.datatypes.DTOEstadoPos;
import org.tallerjava.moduloComercio.datatypes.DTOModificacionComercio;
import org.tallerjava.moduloComercio.datatypes.DTOPassword;
import org.tallerjava.moduloComercio.datatypes.DTOPos;
import org.tallerjava.moduloComercio.datatypes.DTOReclamo;
import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.Pos;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name="API del Módulo Comercio")
@ApplicationScoped
@Path("/comercio")
public class ComercioAPI {
 
    @Inject
    private ServicioComercio servicioComercio;

    //curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/alta -H "Content-Type: application/json" -d '{"direccion":"18 de Julio 111","nombre":"NextRig", "rut": "432151234513212", "contraseña": "1234", "nroCuentaBanco": "112233"}'
    @POST
    @Path("/alta")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response altaComercio(DTOComercio dataComercio) {
        Comercio nuevoComercio = dataComercio.buildComercio();
        Integer resultado = servicioComercio.altaComercio(nuevoComercio);

        if (resultado != -1) {
            return Response
                .ok()
                .build();
        } else {
            return Response
                .serverError()
                .entity("{\"error\": \"Error en el alta del comercio\"}")
                .status(500)
                .build();
        }
    }

    //actualizo solo el rut pero mano los demas campos igual
    //curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/2/modificacion -H "Content-Type: application/json" -d '{"direccion":"18 de Julio 123","nombre":"La verduleria", "rut": "88998899889912"}'
    //actualizo solo la direccion, envio solo ese campo
    //curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/modificacion -H "Content-Type: application/json" -d '{"direccion":"25 de Mayo 111"}'
    //el campo no se actualiza si se envia null
    //curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/modificacion -H "Content-Type: application/json" -d '{"direccion":"18 de Julio 111","nombre":"NextRig", "rut": null}'
    @POST
    @Path("/{idComercio}/modificacion")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificacionComercio(
        @PathParam("idComercio") Integer idComercio,
        DTOModificacionComercio dataComercio) {
        
        boolean resultado = servicioComercio.modificarDatosComercio(
            idComercio, 
            dataComercio.getRut(), 
            dataComercio.getNombre(), 
            dataComercio.getDireccion());

        if (resultado) {
            return Response
                .ok()
                .build();
        } else {
            return Response
                .serverError()
                .entity("{\"error\": \"Error al modificar el comercio\"}")
                .status(500)
                .build();
        }
    }

    //curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/pos/alta -H "Content-Type: application/json" -d '{ "identificador":"pos1"}'
    @POST
    @Path("/{idComercio}/pos/alta")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response altaPos(
        @PathParam("idComercio") Integer idComercio, 
        DTOPos datosPos) {

        Pos nuevoPos = new Pos();
        nuevoPos.setIdentificador(datosPos.getIdentificador());

        Integer resultado = servicioComercio.altaPos(idComercio, nuevoPos);

        if (resultado != -1) {
            return Response
                .ok()
                .build();
        } else {
            return Response
                .serverError()
                .entity("{\"error\": \"Error en el alta del pos\"}")
                .status(500)
                .build();
        }
    }

    //curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/pos/1/estado -H "Content-Type: application/json" -d '{"estado": "false"}'
    @POST
    @Path("/{idComercio}/pos/{idPos}/estado")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cambiarEstadoPos(
        @PathParam("idComercio") int idComercio, 
        @PathParam("idPos") int idPos, 
        DTOEstadoPos estadoPos) {
        boolean resultado = servicioComercio.cambiarEstadoPos(idComercio, idPos, estadoPos.isEstado());

        if (resultado) {
            return Response
                .noContent()
                .build();
        } else {
            return Response
                .serverError()
                .entity("{\"error\": \"Error al modificar el pos\"}")
                .status(500)
                .build();
        }
    }

    //curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/password -H "Content-Type: application/json" -d '{"passwordNueva": "9999"}'
    @POST
    @Path("/{idComercio}/password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cambiarPassword(
        @PathParam("idComercio") Integer idComercio,
        DTOPassword dtoPw) {
            boolean resultado = servicioComercio.cambioContraseña(idComercio, dtoPw.getPasswordNueva());

            if (resultado) {
                return Response
                .noContent()
                .build();
            } else {
                return Response
                .serverError()
                .entity("{\"error\": \"Error al actualizar la contraseña\"}")
                .status(500)
                .build();
            }
    }

    //curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/reclamo -H "Content-Type: application/json" -d '{"contenidoReclamo": "no anda el pos"}'
    @POST
    @Path("/{idComercio}/reclamo")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response realizarReclamo(
        DTOReclamo reclamo, 
        @PathParam("idComercio") Integer idComercio) {

            Integer resultado = servicioComercio.realizarReclamo(idComercio, reclamo.buildReclamo());

            if (resultado != -1) {
                return Response
                .noContent()
                .build();
            } else {
                return Response
                .serverError()
                .entity("{\"error\": \"Error al crear el reclamo\"}")
                .status(500)
                .build();
            }
    }

    //curl http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio
    @GET
    public boolean testAPI() {
        return true;//simplemente para probar que este activa la api, se borra despues
    }

}
