package org.tallerjava.moduloCompra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.tallerjava.moduloCompra.aplicacion.impl.ServicioCompraImpl;
import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.Pos;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;
import org.tallerjava.moduloCompra.dominio.repo.CompraRepositorio;
import org.tallerjava.moduloCompra.interfase.evento.out.PublicadorEvento;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;


public class ServicioCompraImplTest {

    @InjectMocks
    private ServicioCompraImpl servicioCompra;

    @Mock
    private CompraRepositorio repositorio;

    @Mock
    private PublicadorEvento publicador;

    @Mock
    private Comercio comercio;

    @Mock
    private Pos pos;

    @Mock
    private DTOResumenVentas resumenVentas;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests para ingresarNuevaCompra

    @Test
    void testIngresarNuevaCompra_exito_pagoAprobado() {
        Integer idComercio = 1;
        double importe = 10000.0;
        boolean resultado = true;
        Integer idPos = 123;
        double importeVentasActual = 5000.0;

        when(repositorio.buscarPorIdConBloqueo(idComercio)).thenReturn(comercio);
        when(comercio.buscarPosPorId(idPos)).thenReturn(pos);
        when(pos.isHabilitado()).thenReturn(true);
        when(comercio.getImporteVentasDelDia()).thenReturn(importeVentasActual);
        when(repositorio.actualizarComercio(comercio)).thenReturn(true);
        when(repositorio.traerIdCompra(idComercio)).thenReturn(100);

        boolean resultado_operacion = servicioCompra.ingresarNuevaCompra(idComercio, importe, resultado, idPos);

        assertThat(resultado_operacion).isTrue();
        verify(repositorio).buscarPorIdConBloqueo(idComercio);
        verify(comercio).buscarPosPorId(idPos);
        verify(pos).isHabilitado();
        verify(comercio).agregarCompra(any(Compra.class));
        verify(comercio).setImporteVentasDelDia(importeVentasActual + importe);
        verify(repositorio).actualizarComercio(comercio);
        verify(publicador).publicarEventoPago(idComercio, 100, EstadoCompra.APROBADA);
        verify(publicador, never()).publicarEventoPagoError(anyInt(), anyInt(), any(EstadoCompra.class));
    }

    @Test
    void testIngresarNuevaCompra_exito_pagoRechazado() {
        Integer idComercio = 1;
        double importe = 8000.0;
        boolean resultado = false;
        Integer idPos = 456;

        when(repositorio.buscarPorIdConBloqueo(idComercio)).thenReturn(comercio);
        when(comercio.buscarPosPorId(idPos)).thenReturn(pos);
        when(pos.isHabilitado()).thenReturn(true);
        when(repositorio.actualizarComercio(comercio)).thenReturn(true);
        when(repositorio.traerIdCompra(idComercio)).thenReturn(200);

        boolean resultado_operacion = servicioCompra.ingresarNuevaCompra(idComercio, importe, resultado, idPos);

        assertThat(resultado_operacion).isTrue();
        verify(repositorio).buscarPorIdConBloqueo(idComercio);
        verify(comercio).buscarPosPorId(idPos);
        verify(pos).isHabilitado();
        verify(comercio).agregarCompra(any(Compra.class));
        verify(comercio, never()).setImporteVentasDelDia(anyDouble());
        verify(repositorio).actualizarComercio(comercio);
        verify(publicador).publicarEventoPago(idComercio, 200, EstadoCompra.RECHAZADA);
        verify(publicador).publicarEventoPagoError(idComercio, 200, EstadoCompra.RECHAZADA);
    }

    @Test
    void testIngresarNuevaCompra_comercioNoExiste() {
        Integer idComercio = 999;
        double importe = 5000.0;
        boolean resultado = true;
        Integer idPos = 123;

        when(repositorio.buscarPorIdConBloqueo(idComercio)).thenReturn(null);

        boolean resultado_operacion = servicioCompra.ingresarNuevaCompra(idComercio, importe, resultado, idPos);

        assertThat(resultado_operacion).isFalse();
        verify(repositorio).buscarPorIdConBloqueo(idComercio);
        verify(comercio, never()).buscarPosPorId(anyInt());
        verify(repositorio, never()).actualizarComercio(any());
        verify(publicador, never()).publicarEventoPago(anyInt(), anyInt(), any());
    }

    @Test
    void testIngresarNuevaCompra_posNoExiste() {
        Integer idComercio = 1;
        double importe = 7500.0;
        boolean resultado = true;
        Integer idPos = 999;

        when(repositorio.buscarPorIdConBloqueo(idComercio)).thenReturn(comercio);
        when(comercio.buscarPosPorId(idPos)).thenReturn(null);

        boolean resultado_operacion = servicioCompra.ingresarNuevaCompra(idComercio, importe, resultado, idPos);

        assertThat(resultado_operacion).isFalse();
        verify(repositorio).buscarPorIdConBloqueo(idComercio);
        verify(comercio).buscarPosPorId(idPos);
        verify(comercio, never()).agregarCompra(any());
        verify(repositorio, never()).actualizarComercio(any());
        verify(publicador, never()).publicarEventoPago(anyInt(), anyInt(), any());
    }

    @Test
    void testIngresarNuevaCompra_posDeshabilitado() {
        Integer idComercio = 1;
        double importe = 12000.0;
        boolean resultado = true;
        Integer idPos = 123;

        when(repositorio.buscarPorIdConBloqueo(idComercio)).thenReturn(comercio);
        when(comercio.buscarPosPorId(idPos)).thenReturn(pos);
        when(pos.isHabilitado()).thenReturn(false);

        boolean resultado_operacion = servicioCompra.ingresarNuevaCompra(idComercio, importe, resultado, idPos);

        assertThat(resultado_operacion).isFalse();
        verify(repositorio).buscarPorIdConBloqueo(idComercio);
        verify(comercio).buscarPosPorId(idPos);
        verify(pos).isHabilitado();
        verify(comercio, never()).agregarCompra(any());
        verify(repositorio, never()).actualizarComercio(any());
        verify(publicador, never()).publicarEventoPago(anyInt(), anyInt(), any());
    }

    @Test
    void testIngresarNuevaCompra_falloActualizacionRepositorio() {
        Integer idComercio = 1;
        double importe = 15000.0;
        boolean resultado = true;
        Integer idPos = 789;

        when(repositorio.buscarPorIdConBloqueo(idComercio)).thenReturn(comercio);
        when(comercio.buscarPosPorId(idPos)).thenReturn(pos);
        when(pos.isHabilitado()).thenReturn(true);
        when(comercio.getImporteVentasDelDia()).thenReturn(3000.0);
        when(repositorio.actualizarComercio(comercio)).thenReturn(false);

        boolean resultado_operacion = servicioCompra.ingresarNuevaCompra(idComercio, importe, resultado, idPos);

        assertThat(resultado_operacion).isFalse();
        verify(repositorio).buscarPorIdConBloqueo(idComercio);
        verify(comercio).buscarPosPorId(idPos);
        verify(pos).isHabilitado();
        verify(comercio).agregarCompra(any(Compra.class));
        verify(repositorio).actualizarComercio(comercio);
        verify(publicador, never()).publicarEventoPago(anyInt(), anyInt(), any());
        verify(publicador, never()).publicarEventoPagoError(anyInt(), anyInt(), any());
    }

    @Test
    void testIngresarNuevaCompra_pagoRechazadoConFalloActualizacion() {
        Integer idComercio = 1;
        double importe = 6000.0;
        boolean resultado = false;
        Integer idPos = 111;

        when(repositorio.buscarPorIdConBloqueo(idComercio)).thenReturn(comercio);
        when(comercio.buscarPosPorId(idPos)).thenReturn(pos);
        when(pos.isHabilitado()).thenReturn(true);
        when(repositorio.actualizarComercio(comercio)).thenReturn(false);

        boolean resultado_operacion = servicioCompra.ingresarNuevaCompra(idComercio, importe, resultado, idPos);

        assertThat(resultado_operacion).isFalse();
        verify(repositorio).buscarPorIdConBloqueo(idComercio);
        verify(comercio).buscarPosPorId(idPos);
        verify(pos).isHabilitado();
        verify(comercio).agregarCompra(any(Compra.class));
        verify(comercio, never()).setImporteVentasDelDia(anyDouble());
        verify(repositorio).actualizarComercio(comercio);
        verify(publicador, never()).publicarEventoPago(anyInt(), anyInt(), any());
        verify(publicador, never()).publicarEventoPagoError(anyInt(), anyInt(), any());
    }

    // Tests para resumenVentasPorPeriodo

    @Test
    void testResumenVentasPorPeriodo_exito() {
        Integer idComercio = 1;
        LocalDateTime fechaInicio = LocalDateTime.of(2025, 5, 1, 0, 0);
        LocalDateTime fechaFin = LocalDateTime.of(2025, 5, 31, 23, 59);

        when(repositorio.buscarPorId(idComercio)).thenReturn(comercio);
        when(comercio.getResumenVentasPorPeriodo(fechaInicio, fechaFin)).thenReturn(resumenVentas);

        DTOResumenVentas resultado = servicioCompra.resumenVentasPorPeriodo(idComercio, fechaInicio, fechaFin);

        assertThat(resultado).isEqualTo(resumenVentas);
        verify(repositorio).buscarPorId(idComercio);
        verify(comercio).getResumenVentasPorPeriodo(fechaInicio, fechaFin);
    }

    @Test
    void testResumenVentasPorPeriodo_comercioNoExiste() {
        Integer idComercio = 999;
        LocalDateTime fechaInicio = LocalDateTime.of(2025, 5, 1, 0, 0);
        LocalDateTime fechaFin = LocalDateTime.of(2025, 5, 31, 23, 59);

        when(repositorio.buscarPorId(idComercio)).thenReturn(null);

        DTOResumenVentas resultado = servicioCompra.resumenVentasPorPeriodo(idComercio, fechaInicio, fechaFin);

        assertThat(resultado).isNull();
        verify(repositorio).buscarPorId(idComercio);
        verify(comercio, never()).getResumenVentasPorPeriodo(any(), any());
    }

    // Tests para resumenVentasPorEstado

    @Test
    void testResumenVentasPorEstado_exito_estadoAprobada() {
        Integer idComercio = 1;
        EstadoCompra estado = EstadoCompra.APROBADA;

        when(repositorio.buscarPorId(idComercio)).thenReturn(comercio);
        when(comercio.getResumenVentasPorEstado(estado)).thenReturn(resumenVentas);

        DTOResumenVentas resultado = servicioCompra.resumenVentasPorEstado(idComercio, estado);

        assertThat(resultado).isEqualTo(resumenVentas);
        verify(repositorio).buscarPorId(idComercio);
        verify(comercio).getResumenVentasPorEstado(estado);
    }

    @Test
    void testResumenVentasPorEstado_exito_estadoRechazada() {
        Integer idComercio = 1;
        EstadoCompra estado = EstadoCompra.RECHAZADA;

        when(repositorio.buscarPorId(idComercio)).thenReturn(comercio);
        when(comercio.getResumenVentasPorEstado(estado)).thenReturn(resumenVentas);

        DTOResumenVentas resultado = servicioCompra.resumenVentasPorEstado(idComercio, estado);

        assertThat(resultado).isEqualTo(resumenVentas);
        verify(repositorio).buscarPorId(idComercio);
        verify(comercio).getResumenVentasPorEstado(estado);
    }

    @Test
    void testResumenVentasPorEstado_exito_estadoPendiente() {
        Integer idComercio = 1;
        EstadoCompra estado = EstadoCompra.PENDIENTE;

        when(repositorio.buscarPorId(idComercio)).thenReturn(comercio);
        when(comercio.getResumenVentasPorEstado(estado)).thenReturn(resumenVentas);

        DTOResumenVentas resultado = servicioCompra.resumenVentasPorEstado(idComercio, estado);

        assertThat(resultado).isEqualTo(resumenVentas);
        verify(repositorio).buscarPorId(idComercio);
        verify(comercio).getResumenVentasPorEstado(estado);
    }

    @Test
    void testResumenVentasPorEstado_comercioNoExiste() {
        Integer idComercio = 999;
        EstadoCompra estado = EstadoCompra.APROBADA;

        when(repositorio.buscarPorId(idComercio)).thenReturn(null);

        DTOResumenVentas resultado = servicioCompra.resumenVentasPorEstado(idComercio, estado);

        assertThat(resultado).isNull();
        verify(repositorio).buscarPorId(idComercio);
        verify(comercio, never()).getResumenVentasPorEstado(any());
    }

    @Test
    void testResumenVentasPorEstado_estadoNulo() {
        Integer idComercio = 1;
        EstadoCompra estado = null;

        when(repositorio.buscarPorId(idComercio)).thenReturn(comercio);
        when(comercio.getResumenVentasPorEstado(estado)).thenReturn(resumenVentas);

        DTOResumenVentas resultado = servicioCompra.resumenVentasPorEstado(idComercio, estado);

        assertThat(resultado).isEqualTo(resumenVentas);
        verify(repositorio).buscarPorId(idComercio);
        verify(comercio).getResumenVentasPorEstado(estado);
    }

    // Tests para montoActualVendido

    @Test
    void testMontoActualVendido_exito() {
        Integer idComercio = 1;
        double importeEsperado = 25000.0;

        when(repositorio.buscarPorId(idComercio)).thenReturn(comercio);
        when(comercio.getImporteVentasDelDia()).thenReturn(importeEsperado);

        double resultado = servicioCompra.montoActualVendido(idComercio);

        assertThat(resultado).isEqualTo(importeEsperado);
        verify(repositorio).buscarPorId(idComercio);
        verify(comercio).getImporteVentasDelDia();
    }

    @Test
    void testMontoActualVendido_montoVentasCero() {
        Integer idComercio = 1;
        double importeEsperado = 0.0;

        when(repositorio.buscarPorId(idComercio)).thenReturn(comercio);
        when(comercio.getImporteVentasDelDia()).thenReturn(importeEsperado);

        double resultado = servicioCompra.montoActualVendido(idComercio);

        assertThat(resultado).isEqualTo(importeEsperado);
        verify(repositorio).buscarPorId(idComercio);
        verify(comercio).getImporteVentasDelDia();
    }
}