package org.java.tallerjava.moduloComercio.aplicacion;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tallerjava.moduloComercio.aplicacion.impl.ServicioComercioImpl;
import org.tallerjava.moduloComercio.dominio.*;
import org.tallerjava.moduloComercio.dominio.repo.RepositorioComercio;
import org.tallerjava.moduloComercio.interfase.evento.out.PublicadorEvento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Collections;

class ServicioComercioImplTest {

    @Mock
    private RepositorioComercio repositorio;

    @Mock
    private PublicadorEvento publicador;

    @InjectMocks
    private ServicioComercioImpl servicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void altaComercio_deberiaPublicarEventoYRetornarId() {
        Comercio comercio = new Comercio();
        CuentaBancoComercio cuenta = new CuentaBancoComercio();
        
        cuenta.setNumeroCuenta("123");
        cuenta.setId(1);

        comercio.setCuentaBancoComercio(cuenta);
        when(repositorio.guardarComercio(comercio)).thenReturn(1);
        when(repositorio.buscarPorId(1)).thenReturn(comercio);

        int resultado = servicio.altaComercio(comercio);

        assertEquals(1, resultado);
        verify(publicador).publicarEventoComercio(eq(1), eq("123"), eq(1));
    }

    @Test
    void modificarDatosComercio_deberiaActualizarConDatosNuevos() {
        Comercio comercio = new Comercio();
        comercio.setNombre("Viejo");
        comercio.setDireccion("Vieja");
        comercio.setRut("123");

        when(repositorio.buscarPorId(1)).thenReturn(comercio);
        when(repositorio.actualizarComercio(any())).thenReturn(true);

        boolean resultado = servicio.modificarDatosComercio(1, "456", "Nuevo", "Nueva");

        assertTrue(resultado);
        assertEquals("456", comercio.getRut());
        assertEquals("Nuevo", comercio.getNombre());
        assertEquals("Nueva", comercio.getDireccion());
    }

    @Test
    void altaPos_deberiaAgregarPosYRetornarId() {
        Comercio comercio = mock(Comercio.class);
        Pos pos = new Pos();
        pos.setIdentificador("POS1");
        pos.setId(100);

        when(repositorio.buscarPorId(1)).thenReturn(comercio);
        when(repositorio.actualizarComercio(comercio)).thenReturn(true);
        when(comercio.buscarPosPorIdentificador("POS1")).thenReturn(pos);
        when(repositorio.buscarPorId(1)).thenReturn(comercio);

        int resultado = servicio.altaPos(1, pos);

        assertEquals(pos.getId(), resultado);
        verify(comercio).agregarPos(pos);
        //verify(pos).setComercio(comercio);
    }

    @Test
    void cambiarEstadoPos_deberiaActualizarEstadoCorrectamente() {
        Comercio comercio = mock(Comercio.class);
        Pos pos = new Pos();
        pos.setId(99);
        pos.setHabilitado(false);

        when(repositorio.buscarPorId(1)).thenReturn(comercio);
        when(comercio.buscarPosPorId(99)).thenReturn(pos);
        when(repositorio.actualizarComercio(comercio)).thenReturn(true);

        boolean resultado = servicio.cambiarEstadoPos(1, 99, true);

        assertTrue(resultado);
        assertTrue(pos.isHabilitado());
    }

    @Test
    void cambioContraseña_deberiaActualizarContraseña() {
        Comercio comercio = new Comercio();

        when(repositorio.buscarPorId(1)).thenReturn(comercio);
        when(repositorio.actualizarComercio(comercio)).thenReturn(true);

        boolean resultado = servicio.cambioContraseña(1, "nuevaPass");

        assertTrue(resultado);
        assertEquals("nuevaPass", comercio.getContraseña());
    }

    @Test
    void realizarReclamo_deberiaAgregarYRetornarId() {
        Comercio comercio = new Comercio();
        Reclamo reclamo = new Reclamo("texto");
        reclamo.setId(42);
        comercio.agregarReclamo(reclamo);

        when(repositorio.buscarPorId(1)).thenReturn(comercio);
        when(repositorio.actualizarComercio(comercio)).thenReturn(true);
        when(repositorio.buscarPorId(1)).thenReturn(comercio);

        int resultado = servicio.realizarReclamo(1, reclamo);

        assertEquals(42, resultado);
        assertTrue(comercio.getReclamos().contains(reclamo));
    }
}