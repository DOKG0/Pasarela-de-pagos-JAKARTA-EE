package org.tallerjava.moduloComercio.infraestructura.seguridad.interceptors;

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
                .status(404)
                .build();
            }

            if (usuarioEsAdmin) {
                return invocationContext.proceed();
            }
            
            if (!comercio.getUsuario().equals(nombreUsuario)) {
                return Response
                .serverError()
                .entity("{\"error\": \"El comercio no pertenece al usuario\"}")
                .status(403)
                .build();
            }

            return invocationContext.proceed();

        } catch (Exception e) {
            System.err.println("Falla en el interceptor de validación de credenciales del comercio/admin.");
            System.err.println(e.getMessage());
            return Response
                .serverError()
                .entity("{\"error\": \"Error al validar las credenciales del comercio/admin\"}")
                .status(500)
                .build();
        }

    }
}
