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
import org.tallerjava.moduloComercio.infraestructura.seguridad.interceptors.ApiInterceptorCredencialesComercio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

//Swagger
@Tag(name="API del Módulo Comercio")
@Server(url="http://localhost:8080/TallerJakartaEEPasarelaPagos/api")
//Logica
@ApplicationScoped
@Path("/comercio")
public class ComercioAPI {

    @Inject
    private ServicioComercio servicioComercio;

    //Swagger
    @Operation(
        summary="Da de alta un nuevo comercio",
        description="Un comercio se registra ingresando su número de cuenta de banco, nombre de usuario y contraseña. Opcionalmente podrá agregar su dirección, RUT y nombre del comercio.")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "Operación de alta exitosa"),
        @ApiResponse(responseCode = "400", description = "Fallo en el alta por falta de datos requeridos"),
        @ApiResponse(responseCode = "500", description = "Fallo en el alta por error del servidor. Causas: nombre de usuario ya existe, falla en la asignación del grupo.")})
    @RequestBody(
        description = "Estructura del request",
        required = true,
        content = @Content(schema = @Schema(implementation = DTOComercio.class)))
    //Logica
    @POST
    @Path("/alta")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response altaComercio(DTOComercio dataComercio) {

        if (dataComercio.getUsuario() == null ||
            dataComercio.getPassword() == null ||
            dataComercio.getNroCuentaBanco() == null) {
                return Response
                .serverError()
                .entity("{\"error\": \"Los campos usuario, nroCuentaBanco y password son requeridos.\"}")
                .status(400)
                .build();
        }

        Comercio nuevoComercio = dataComercio.buildComercio();
        Integer resultado = servicioComercio.altaComercio(nuevoComercio, dataComercio.getPassword());

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

    //Swagger
    @Operation(
        summary="Modificación de datos de un comercio",
        description="Un comercio puede modificar algunos de sus datos, como el nombre, rut y dirección. Un comercio deberá autenticarse con su nombre de usuario y contraseña para poder modificar sus datos.")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "Operación de modificación exitosa"),
        @ApiResponse(responseCode = "403", description = "Fallo por falta de credenciales o credenciales incorrectas"),
        @ApiResponse(responseCode = "404", description = "El comercio con el id provisionado no existe"),
        @ApiResponse(responseCode = "500", description = "Fallo en la modificación del comercio")})
    @RequestBody(
        description = "Estructura del request",
        required = true,
        content = @Content(schema = @Schema(implementation = DTOModificacionComercio.class)))
    //Logica
    @POST
    @Path("/{idComercio}/modificacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("comercio")
    @ApiInterceptorCredencialesComercio
    public Response modificacionComercio(
        @PathParam("idComercio") Integer idComercio,
        @Context SecurityContext securityContext,
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

    //Swagger
    @Operation(
        summary="Alta de un POS de un comercio",
        description="Un comercio puede dar da alta un POS. Para ello es necesario que provea el identificador del mismo y deberá proveer las credenciales de usuario.")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "Operación de alta exitosa"),
        @ApiResponse(responseCode = "400", description = "Fallo por falta de datos requeridos"),
        @ApiResponse(responseCode = "403", description = "Fallo por falta de credenciales o credenciales incorrectas"),
        @ApiResponse(responseCode = "404", description = "No se encontró el comercio con el id provisionado"),
        @ApiResponse(responseCode = "500", description = "Fallo en el servidor")})
    @RequestBody(
        description = "Estructura del request",
        required = true,
        content = @Content(schema = @Schema(implementation = DTOPos.class)))
    //Logica
    @POST
    @Path("/{idComercio}/pos/alta")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("comercio")
    @ApiInterceptorCredencialesComercio
    public Response altaPos(
        @PathParam("idComercio") Integer idComercio,
        @Context SecurityContext securityContext,
        DTOPos datosPos) {

        if (datosPos.getIdentificador() == null) {
            return Response
                .serverError()
                .entity("{\"error\": \"El identificador del pos es un campo requerido\"}")
                .status(400)
                .build();
        }

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

    //Swagger
    @Operation(
        summary="Modificación del estado de un POS",
        description="Un comercio puede modificar el estado de un POS, habilitándolo o deshabilitándolo. Para ello es necesario que provea el identificador del comercio y del POS y deberá proveer las credenciales de usuario.")
    @ApiResponses(value={
        @ApiResponse(responseCode = "204", description = "Operación de modificación exitosa"),
        @ApiResponse(responseCode = "403", description = "Fallo por falta de credenciales o credenciales incorrectas"),
        @ApiResponse(responseCode = "404", description = "No se encontró un comercio con el id provisionado"),
        @ApiResponse(responseCode = "500", description = "Fallo en el servidor o el pos no se encontró en el sistema")})
    @RequestBody(
        description = "Estructura del request",
        required = true,
        content = @Content(schema = @Schema(implementation = DTOEstadoPos.class)))
    //Logica
    @POST
    @Path("/{idComercio}/pos/{idPos}/estado")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"comercio"})
    @ApiInterceptorCredencialesComercio
    public Response cambiarEstadoPos(
        @PathParam("idComercio") Integer idComercio, 
        @Context SecurityContext securityContext,
        @PathParam("idPos") Integer idPos, 
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

    //Swagger
    @Operation(
        summary="Cambio de contraseña de un comercio",
        description="Un comercio puede modificar su contraseña. Para ello es necesario que provea el identificador del mismo y deberá proveer las credenciales de usuario.")
    @ApiResponses(value={
        @ApiResponse(responseCode = "204", description = "Operación de modificación exitosa"),
        @ApiResponse(responseCode = "400", description = "Fallo por falta de datos requeridos"),
        @ApiResponse(responseCode = "403", description = "Fallo por falta de credenciales o credenciales incorrectas"),
        @ApiResponse(responseCode = "404", description = "No se encontró un comercio con el id provisionado"),
        @ApiResponse(responseCode = "500", description = "Fallo en el servidor")})
    @RequestBody(
        description = "Estructura del request",
        required = true,
        content = @Content(schema = @Schema(implementation = DTOPassword.class)))
    //Logica
    @POST
    @Path("/{idComercio}/password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("comercio")
    @ApiInterceptorCredencialesComercio
    public Response cambiarPassword(
        @PathParam("idComercio") Integer idComercio,
        @Context SecurityContext securityContext,
        DTOPassword dtoPw) {

            if (dtoPw.getPasswordNueva() == null) {
                return Response
                .serverError()
                .entity("{\"error\": \"La nueva contraseña es un campo requerido\"}")
                .status(400)
                .build();
            }

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

    //Swagger
    @Operation(
        summary="Ingreso de un nuevo reclamo por un comercio",
        description="Un comercio puede ingresar un reclamo. Para ello es necesario que provea el identificador del mismo y deberá proveer las credenciales de usuario.")
    @ApiResponses(value={
        @ApiResponse(responseCode = "204", description = "Operación de alta exitosa"),
        @ApiResponse(responseCode = "400", description = "Fallo por falta de datos requeridos"),
        @ApiResponse(responseCode = "403", description = "Fallo por falta de credenciales o credenciales incorrectas"),
        @ApiResponse(responseCode = "404", description = "No se encontró un comercio con el id provisionado"),
        @ApiResponse(responseCode = "500", description = "Fallo en el servidor")})
    @RequestBody(
        description = "Estructura del request",
        required = true,
        content = @Content(schema = @Schema(implementation = DTOReclamo.class)))
    //Logica
    @POST
    @Path("/{idComercio}/reclamo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("comercio")
    @ApiInterceptorCredencialesComercio
    public Response realizarReclamo(
        @PathParam("idComercio") Integer idComercio,
        @Context SecurityContext securityContext,
        DTOReclamo reclamo) {

            if (reclamo.getContenidoReclamo() == null) {
                return Response
                .serverError()
                .entity("{\"error\": \"El contenido del reclamo es un dato requerido\"}")
                .status(400)
                .build();
            }

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

}
