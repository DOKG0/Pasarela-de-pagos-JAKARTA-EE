package org.tallerjava.moduloSeguridad.infraestructura.seguridad;

import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;

@BasicAuthenticationMechanismDefinition(realmName = "ApplicationRealm")
@DeclareRoles({"comercio", "admin", "servicioExterno"})
@ApplicationScoped
public class SeguridadConfiguracion {
    
}
