package org.tallerjava.moduloCompra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.tallerjava.moduloCompra.aplicacion.ServicioCompra;
import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.Tarjeta;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOPago;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;
import org.tallerjava.moduloCompra.interfase.remota.CompraAPI;

import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CompraApiTest {
    
    @InjectMocks
    private CompraAPI compraAPI;

    @Mock
    private ServicioCompra servicioCompra;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests para procesarPago

    //Verifica que la API procese un pago valido
    @Test
    void testProcesarPago_exito() {
        DTOPago dtoPago = mock(DTOPago.class);
        when(dtoPago.getFechaVtoTarjeta()).thenReturn("2025-05-17");
        when(dtoPago.getNroTarjeta()).thenReturn(123);
        when(dtoPago.getMarcaTarjeta()).thenReturn("visa");
        when(dtoPago.getImporte()).thenReturn(10000.0);

        when(servicioCompra.procesarPago(anyInt(), anyDouble(), any(Tarjeta.class))).thenReturn(true);

        Response response = compraAPI.procesarPago(1, dtoPago);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(servicioCompra).procesarPago(eq(1), eq(10000.0), any(Tarjeta.class));
    }

    //Confirma que la API maneja un pago rechazado
    @Test
    void testProcesarPago_rechazado() {
        DTOPago dtoPago = mock(DTOPago.class);
        when(dtoPago.getFechaVtoTarjeta()).thenReturn("2025-05-17");
        when(dtoPago.getNroTarjeta()).thenReturn(123);
        when(dtoPago.getMarcaTarjeta()).thenReturn("visa");
        when(dtoPago.getImporte()).thenReturn(10000.0);

        when(servicioCompra.procesarPago(anyInt(), anyDouble(), any(Tarjeta.class))).thenReturn(false);

        Response response = compraAPI.procesarPago(1, dtoPago);

        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("El pago fue rechazado"));
    }

    //Prueba que la API detecte errores en el formato de fecha de la tarjeta.
    @Test
    void testProcesarPago_fechaInvalida() {
        DTOPago dtoPago = mock(DTOPago.class);
        when(dtoPago.getFechaVtoTarjeta()).thenReturn("fecha-invalida");

        Response response = compraAPI.procesarPago(1, dtoPago);

        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Fecha de vencimiento de tarjeta invalida"));
        verify(servicioCompra, never()).procesarPago(anyInt(), anyDouble(), any(Tarjeta.class));
    }

    // Tests para obtenerResumenDeVentasPorPeriodo
    //Verifica un resumen de ventas por periodo
    @Test
    void testObtenerResumenDeVentasPorPeriodo_exito() {
        String fechaInicioStr = "2025-05-18";
        String fechaFinStr = "2025-05-19";
        LocalDateTime fechaInicio = LocalDate.parse(fechaInicioStr).atStartOfDay();
        LocalDateTime fechaFin = LocalDate.parse(fechaFinStr).atTime(23,59,59);
        
        DTOResumenVentas resumenMock = mock(DTOResumenVentas.class);
        when(servicioCompra.resumenVentasPorPeriodo(anyInt(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(resumenMock);

        Response response = compraAPI.obtenerResumenDeVentasPorPeriodo(1, fechaInicioStr, fechaFinStr);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(resumenMock, response.getEntity());
        verify(servicioCompra).resumenVentasPorPeriodo(eq(1), eq(fechaInicio), eq(fechaFin));
    }

    //Prueba el manejo de errores cuando el servicio no puede generar un resumen
    @Test
    void testObtenerResumenDeVentasPorPeriodo_errorResumen() {
        String fechaInicioStr = "2025-05-18";
        String fechaFinStr = "2025-05-19";
        
        when(servicioCompra.resumenVentasPorPeriodo(anyInt(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(null);

        Response response = compraAPI.obtenerResumenDeVentasPorPeriodo(1, fechaInicioStr, fechaFinStr);

        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al generar el resumen"));
    }

    //Confirma que la API detecta fechas en formato incorrecto.
    @Test
    void testObtenerResumenDeVentasPorPeriodo_fechasInvalidas() {
        String fechaInicioStr = "fecha-invalida";
        String fechaFinStr = "fecha-invalida";

        Response response = compraAPI.obtenerResumenDeVentasPorPeriodo(1, fechaInicioStr, fechaFinStr);

        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al procesar los parametros"));
        verify(servicioCompra, never()).resumenVentasPorPeriodo(anyInt(), any(), any());
    }

    // Tests para obtenerResumenDeVentasDiario
    //Verifica que la API devuelva un resumen por estado.
    @Test
    void testObtenerResumenDeVentasDiario_exito() {
        EstadoCompra estado = EstadoCompra.APROBADA;
        DTOResumenVentas resumenMock = mock(DTOResumenVentas.class);
        when(servicioCompra.resumenVentasPorEstado(anyInt(), any(EstadoCompra.class)))
            .thenReturn(resumenMock);

        Response response = compraAPI.obtenerResumenDeVentasDiario(1, estado);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(resumenMock, response.getEntity());
        verify(servicioCompra).resumenVentasPorEstado(eq(1), eq(estado));
    }
    
    //Prueba el manejo de errores cuando no se puede generar un resumen por estado.
    @Test
    void testObtenerResumenDeVentasDiario_errorResumen() {
        EstadoCompra estado = EstadoCompra.RECHAZADA;
        when(servicioCompra.resumenVentasPorEstado(anyInt(), any(EstadoCompra.class)))
            .thenReturn(null);

        Response response = compraAPI.obtenerResumenDeVentasDiario(1, estado);

        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al generar el resumen"));
    }
}