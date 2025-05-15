package org.tallerjava.moduloComercio.interfase.remota;

import org.tallerjava.moduloComercio.aplicacion.ServicioComercio;
import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.Pos;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/comercio")
public class ComercioAPI {
 
    @Inject
    private ServicioComercio servicioComercio;

    //curl -X POST -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/alta-comercio -H "Content-Type: application/json" -d '{"direccion":"18 de Julio 123","nombre":"NextRig", "rut": "123451234512345", "contraseña": "1234"}'
    @POST
    @Path("/alta-comercio")
    @Consumes(MediaType.APPLICATION_JSON)
    public Integer altaComercio(DTOComercio dataComercio) {
        Comercio nuevoComercio = dataComercio.buildComercio();

        return servicioComercio.altaComercio(nuevoComercio);
    }

    //curl -X POST -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/alta-pos -H "Content-Type: application/json" -d '{"idComercio": "1","identificador":"pos1"}'
    @POST
    @Path("/alta-pos")
    @Consumes(MediaType.APPLICATION_JSON)
    public Integer altaPos(DTOPos datosPos) {
        Pos nuevoPos = new Pos();
        nuevoPos.setIdentificador(datosPos.getIdentificador());

        return servicioComercio.altaPos(datosPos.getIdComercio(), nuevoPos);
    }

    //curl http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio
    @GET
    public boolean testAPI() {
        return true;//simplemente para probar que este activa la api, se borra despues
    }

}
