package org.tallerjava.moduloComercio.infraestructura.interceptors;

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
@ApiInterceptor
public class ApiInterceptorCredenciales {
    
    @Inject
    RepositorioComercio repositorioComercio;

    @AroundInvoke
    public Object validarCredencialesComercio(InvocationContext invocationContext) {
        try {
            
            Integer idComercio = (Integer) invocationContext.getParameters()[0];
            SecurityContext securityContext = (SecurityContext) invocationContext.getParameters()[1];
            String usuario = securityContext.getUserPrincipal().getName();

            Comercio comercio = repositorioComercio.buscarPorId(idComercio);

            if (comercio.getUsuario().equals(usuario)) {
                return invocationContext.proceed();
            } else {
                return Response
                .serverError()
                .entity("{\"error\": \"El comercio no pertenece al usuario\"}")
                .status(500)
                .build();
            }
            
            
        } catch (Exception e) {
            System.err.println("Falla en el interceptor de validación de credenciales del comercio.");
            return Response
                .serverError()
                .entity("{\"error\": \"Error al validar las credenciales del comercio\"}")
                .status(500)
                .build();
        }
    }

}
