package org.tallerjava.moduloComercio.infraestructura.seguridad.interceptors;

import java.util.logging.Logger;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.repo.RepositorioComercio;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Interceptor
@Priority(1)
@ApiInterceptorCredencialesAdminComercio
public class ApiInterceptorCredencialesAdminComercioImpl {

    @Inject
    RepositorioComercio repositorioComercio;

    private static final Logger LOG = Logger.getLogger(ApiInterceptorCredencialesAdminComercioImpl.class.getName());

    @AroundInvoke
    public Object validarCredenciales(InvocationContext invocationContext) {

        try {
            //el interceptor requiere que el id del comercio sea el primer parametro de la operacion y el security context el segundo parametro de la operacion
            Integer idComercio = (Integer) invocationContext.getParameters()[0];
            SecurityContext securityContext = (SecurityContext) invocationContext.getParameters()[1];
            String nombreUsuario = securityContext.getUserPrincipal().getName();
            boolean usuarioEsAdmin = securityContext.isUserInRole("admin");
            Comercio comercio = repositorioComercio.buscarPorId(idComercio);
                
            if (comercio == null) {
                return Response
                .serverError()
                .entity("{\"error\": \"El comercio no existe\"}")
                .status(Response.Status.NOT_FOUND)
                .build();
            }

            if (usuarioEsAdmin) {
                return invocationContext.proceed();
            }
            
            if (!comercio.getUsuario().equals(nombreUsuario)) {
                LOG.info("[COMERCIO][ApiInterceptorCredencialesAdminComercioImpl] El usuario " + nombreUsuario + " ha intentado acceder al Comercio " + comercio.getId());

                return Response
                .serverError()
                .entity("{\"error\": \"El comercio no pertenece al usuario\"}")
                .status(Response.Status.FORBIDDEN)
                .build();
            }

            return invocationContext.proceed();

        } catch (Exception e) {
            LOG.warning("[COMERCIO][ApiInterceptorCredencialesAdminComercioImpl] Falla en el interceptor de validación de credenciales del comercio/admin.");
            e.printStackTrace();

            return Response
                .serverError()
                .entity("{\"error\": \"Error al validar las credenciales del comercio/admin\"}")
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        }

    }
}
