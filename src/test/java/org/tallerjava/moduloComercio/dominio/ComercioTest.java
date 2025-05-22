package org.tallerjava.moduloComercio.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.Pos;
import org.tallerjava.moduloComercio.dominio.Reclamo;

public class ComercioTest {

    @Test
    void testCreacionComercioValido() {
        Comercio comercio = new Comercio("RUT123", "Mi Tienda", "Dirección 123", "123");
        assertNotNull(comercio);
        assertEquals("RUT123", comercio.getRut());
        assertEquals("Mi Tienda", comercio.getNombre());
        assertTrue(comercio.getPoses().isEmpty());
    }

    @Test
    void testCambioContraseñaValido() {
        Comercio comercio = new Comercio("RUT123", "Mi Tienda", "Dirección 123", "123");
        String nuevaPass = "NuevaPass123!";
        comercio.setContraseña(nuevaPass);

        assertEquals(nuevaPass, comercio.getContraseña());
    }

    @Test
    void testAgregarPosCorrectamente() {
        Comercio comercio = new Comercio();
        Pos pos = new Pos();
        pos.setIdentificador("POS-001");
        
        comercio.agregarPos(pos);
        
        assertEquals(1, comercio.getPoses().size());
        assertEquals("POS-001", comercio.getPoses().get(0).getIdentificador());
    }

    @Test
    void testBuscarPosPorIdentificador() {
        Comercio comercio = new Comercio();
        Pos pos1 = new Pos();
        pos1.setIdentificador("POS-001");
        Pos pos2 = new Pos();
        pos2.setIdentificador("POS-002");
        
        comercio.agregarPos(pos1);
        comercio.agregarPos(pos2);
        
        Pos encontrado = comercio.buscarPosPorIdentificador("POS-002");
        assertNotNull(encontrado);
        assertEquals("POS-002", encontrado.getIdentificador());
    }

    @Test
    void testAgregarReclamo() {
        Comercio comercio = new Comercio();
        Reclamo reclamo = new Reclamo("Problema con pago");
        
        comercio.agregarReclamo(reclamo);
        
        assertEquals(1, comercio.getReclamos().size());
        assertEquals("Problema con pago", comercio.getReclamos().get(0).getTexto());
    }

    @Test
    void testRetornarNullSiNoEncuentraPos() {
        Comercio comercio = new Comercio();
        assertNull(comercio.buscarPosPorIdentificador("POS-999"));
    }
}

