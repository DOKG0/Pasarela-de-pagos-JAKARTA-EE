package org.java.tallerjava.moduloComercio.dominio;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.tallerjava.moduloComercio.dominio.Pos;

public class PosTest {
    
    @Test
    void deberiaCrearPosHabilitadoPorDefecto() {
        Pos pos = new Pos();
        pos.setIdentificador("POS-001");
        
        assertEquals("POS-001", pos.getIdentificador());
        assertTrue(pos.isHabilitado());
    }

    @Test
    void deberiaCambiarEstadoPos() {
        Pos pos = new Pos();
        pos.setHabilitado(false);
        
        assertFalse(pos.isHabilitado());
    }
    
}
