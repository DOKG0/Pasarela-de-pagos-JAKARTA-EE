package org.tallerjava.moduloSeguridad.infraestructura.seguridad.identitystore;

import org.tallerjava.moduloSeguridad.dominio.Usuario;
import org.tallerjava.moduloSeguridad.infraestructura.persistencia.RepositorioSeguridad;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

@ApplicationScoped
public class ValidadorCredenciales implements IdentityStore {

	@Inject
	RepositorioSeguridad repositorio;

	@Override
	public CredentialValidationResult validate(Credential credential) {
		CredentialValidationResult resultado = CredentialValidationResult.INVALID_RESULT;
		
		UsernamePasswordCredential credencial = (UsernamePasswordCredential) credential;

		String nombreUsuario = credencial.getCaller();
		String password = credencial.getPasswordAsString();

		Usuario usuario = repositorio.buscarUsuario(nombreUsuario); 

		if (usuario != null) {
			if (usuario.passwordCorrecta(password)) {
				resultado =  new CredentialValidationResult(
					nombreUsuario, 
					usuario.obtenerGrupoComoSet());
			}
		}

		return resultado;
	
	}

}
