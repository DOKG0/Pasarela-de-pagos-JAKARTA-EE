package org.java.tallerjava.moduloComercio.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.tallerjava.moduloComercio.dominio.EstadoReclamo;
import org.tallerjava.moduloComercio.dominio.Reclamo;

public class ReclamoTest {
    
    @Test
    void deberiaCrearReclamoConEstadoPendiente() {
        Reclamo reclamo = new Reclamo("Producto defectuoso");
        
        assertEquals("Producto defectuoso", reclamo.getTexto());
        assertEquals(EstadoReclamo.PENDIENTE, reclamo.getEstado());
        assertNotNull(reclamo.getFecha());
        assertTrue(reclamo.getFecha().isBefore(LocalDateTime.now().plusMinutes(1)));
    }

    @Test
    void deberiaLanzarExcepcionConTextoVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Reclamo("");
        });
    }

}
