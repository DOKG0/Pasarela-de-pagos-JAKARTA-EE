package org.tallerjava.moduloSeguridad.infraestructura.interceptors;

import org.tallerjava.moduloSeguridad.dominio.Comercio;
import org.tallerjava.moduloSeguridad.infraestructura.persistencia.RepositorioSeguridad;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Interceptor
@Priority(1)
@ApiInterceptorCredencialesComercio
public class ApiInterceptorCredencialesComercioImpl {
    
    @Inject
    RepositorioSeguridad repositorioSeguridad;

    @AroundInvoke
    public Object validarCredencialesComercio(InvocationContext invocationContext) {
        try {
            //el interceptor requiere que el id del comercio sea el primer parametro de la operacion y el security context el segundo parametro de la operacion
            Integer idComercio = (Integer) invocationContext.getParameters()[0];
            SecurityContext securityContext = (SecurityContext) invocationContext.getParameters()[1];
            String nombreUsuario = securityContext.getUserPrincipal().getName();

            Comercio comercio = repositorioSeguridad.buscarComercio(idComercio);
            
            if (comercio == null) {
                return Response
                .serverError()
                .entity("{\"error\": \"El comercio no existe\"}")
                .status(404)
                .build();
            }

            if (!comercio.getUsuario().getUsuario().equals(nombreUsuario)) {
                return Response
                .serverError()
                .entity("{\"error\": \"El comercio no pertenece al usuario\"}")
                .status(403)
                .build();
            }

            return invocationContext.proceed();
                        
        } catch (Exception e) {
            System.err.println("Falla en el interceptor de validación de credenciales del comercio.");
            System.err.println(e.getMessage());
            return Response
                .serverError()
                .entity("{\"error\": \"Error al validar las credenciales del comercio\"}")
                .status(500)
                .build();
        }
    }

}
