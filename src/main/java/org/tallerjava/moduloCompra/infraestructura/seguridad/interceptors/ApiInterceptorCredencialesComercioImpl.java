package org.tallerjava.moduloCompra.infraestructura.seguridad.interceptors;

import java.util.logging.Logger;

import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.repo.CompraRepositorio;

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
    CompraRepositorio repositorioCompra;

    private static final Logger LOG = Logger.getLogger(ApiInterceptorCredencialesComercioImpl.class.getName());

    @AroundInvoke
    public Object validarCredencialesComercio(InvocationContext invocationContext) {
        try {
            //el interceptor requiere que el id del comercio sea el primer parametro de la operacion y el security context el segundo parametro de la operacion
            Integer idComercio = (Integer) invocationContext.getParameters()[0];
            SecurityContext securityContext = (SecurityContext) invocationContext.getParameters()[1];
            String nombreUsuario = securityContext.getUserPrincipal().getName();

            Comercio comercio = repositorioCompra.buscarPorId(idComercio);
            
            if (comercio == null) {
                return Response
                .serverError()
                .entity("{\"error\": \"El comercio no existe\"}")
                .status(404)
                .build();
            }

            if (!comercio.getUsuario().equals(nombreUsuario)) {
                LOG.info("[COMPRA][ApiInterceptorCredencialesAdminComercioImpl] El usuario " + nombreUsuario + " ha intentado acceder al Comercio " + comercio.getId());

                return Response
                .serverError()
                .entity("{\"error\": \"El comercio no pertenece al usuario\"}")
                .status(403)
                .build();
            }

            return invocationContext.proceed();

        } catch (Exception e) {
            LOG.warning("[COMPRA][ApiInterceptorCredencialesComercioImpl] Falla en el interceptor de validación de credenciales del comercio.");
            e.printStackTrace();

            return Response
                .serverError()
                .entity("{\"error\": \"Error al validar las credenciales del comercio\"}")
                .status(500)
                .build();
        }
    }

}
