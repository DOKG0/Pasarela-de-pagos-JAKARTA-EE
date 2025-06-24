package org.tallerjava.moduloCompra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.tallerjava.moduloCompra.aplicacion.ServicioCompra;
import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOTransferencia;
import org.tallerjava.moduloCompra.interfase.remota.ClienteHttpCompra;
import org.tallerjava.moduloCompra.interfase.remota.CompraAPI;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class CompraApiTest {
    
    @InjectMocks
    private CompraAPI compraAPI;

    @Mock
    private ServicioCompra servicioCompra;

    @Mock
    private ClienteHttpCompra httpClient;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests para procesarPago

    @Test
    void testProcesarPago_exito_pagoAceptado() {
        DTOTransferencia datosCompra = mock(DTOTransferencia.class);
        when(datosCompra.getIdComercio()).thenReturn(1);
        when(datosCompra.getMonto()).thenReturn(10000.0);
        when(datosCompra.getIdPos()).thenReturn(123);
        
        when(httpClient.enviarSolicitudPago(datosCompra)).thenReturn(true);
        when(servicioCompra.ingresarNuevaCompra(1, 10000.0, true, 123)).thenReturn(true);

        Response response = compraAPI.procesarPago(1, securityContext, datosCompra);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("El pago fue aceptado"));
        verify(httpClient).enviarSolicitudPago(datosCompra);
        verify(servicioCompra).ingresarNuevaCompra(1, 10000.0, true, 123);
    }

    @Test
    void testProcesarPago_servicioExterno_rechaza_pero_compra_registrada() {
        DTOTransferencia datosCompra = mock(DTOTransferencia.class);
        when(datosCompra.getIdComercio()).thenReturn(1);
        when(datosCompra.getMonto()).thenReturn(5000.0);
        when(datosCompra.getIdPos()).thenReturn(456);
        
        when(httpClient.enviarSolicitudPago(datosCompra)).thenReturn(false);
        when(servicioCompra.ingresarNuevaCompra(1, 5000.0, false, 456)).thenReturn(true);

        Response response = compraAPI.procesarPago(1, securityContext, datosCompra);

        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("El pago fue rechazado por el servicio externo"));
        verify(httpClient).enviarSolicitudPago(datosCompra);
        verify(servicioCompra).ingresarNuevaCompra(1, 5000.0, false, 456);
    }

    @Test
    void testProcesarPago_falloRegistroCompra() {
        DTOTransferencia datosCompra = mock(DTOTransferencia.class);
        when(datosCompra.getIdComercio()).thenReturn(1);
        when(datosCompra.getMonto()).thenReturn(15000.0);
        when(datosCompra.getIdPos()).thenReturn(789);
        
        when(httpClient.enviarSolicitudPago(datosCompra)).thenReturn(true);
        when(servicioCompra.ingresarNuevaCompra(1, 15000.0, true, 789)).thenReturn(false);

        Response response = compraAPI.procesarPago(1, securityContext, datosCompra);

        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("El pago fue rechazado por la pasarela de pagos"));
        verify(httpClient).enviarSolicitudPago(datosCompra);
        verify(servicioCompra).ingresarNuevaCompra(1, 15000.0, true, 789);
    }

    // Tests para obtenerResumenDeVentasPorPeriodo

    @Test
    void testObtenerResumenDeVentasPorPeriodo_exito() {
        String fechaInicioStr = "2025-05-18";
        String fechaFinStr = "2025-05-19";
        LocalDateTime fechaInicio = LocalDate.parse(fechaInicioStr).atStartOfDay();
        LocalDateTime fechaFin = LocalDate.parse(fechaFinStr).atTime(23, 59, 59);
        
        DTOResumenVentas resumenMock = mock(DTOResumenVentas.class);
        when(servicioCompra.resumenVentasPorPeriodo(1, fechaInicio, fechaFin)).thenReturn(resumenMock);

        Response response = compraAPI.obtenerResumenDeVentasPorPeriodo(1, securityContext, fechaInicioStr, fechaFinStr);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(resumenMock, response.getEntity());
        verify(servicioCompra).resumenVentasPorPeriodo(1, fechaInicio, fechaFin);
    }

    @Test
    void testObtenerResumenDeVentasPorPeriodo_errorResumen() {
        String fechaInicioStr = "2025-05-18";
        String fechaFinStr = "2025-05-19";
        LocalDateTime fechaInicio = LocalDate.parse(fechaInicioStr).atStartOfDay();
        LocalDateTime fechaFin = LocalDate.parse(fechaFinStr).atTime(23, 59, 59);
        
        when(servicioCompra.resumenVentasPorPeriodo(1, fechaInicio, fechaFin)).thenReturn(null);

        Response response = compraAPI.obtenerResumenDeVentasPorPeriodo(1, securityContext, fechaInicioStr, fechaFinStr);

        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al generar el resumen"));
        verify(servicioCompra).resumenVentasPorPeriodo(1, fechaInicio, fechaFin);
    }

    @Test
    void testObtenerResumenDeVentasPorPeriodo_fechasInvalidas() {
        String fechaInicioStr = "fecha-invalida";
        String fechaFinStr = "otra-fecha-invalida";

        Response response = compraAPI.obtenerResumenDeVentasPorPeriodo(1, securityContext, fechaInicioStr, fechaFinStr);

        assertEquals(400, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al procesar los parametros"));
        verify(servicioCompra, never()).resumenVentasPorPeriodo(anyInt(), any(), any());
    }

    @Test
    void testObtenerResumenDeVentasPorPeriodo_fechaInicioNula() {
        String fechaInicioStr = null;
        String fechaFinStr = "2025-05-19";

        Response response = compraAPI.obtenerResumenDeVentasPorPeriodo(1, securityContext, fechaInicioStr, fechaFinStr);

        assertEquals(400, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al procesar los parametros"));
        verify(servicioCompra, never()).resumenVentasPorPeriodo(anyInt(), any(), any());
    }

    @Test
    void testObtenerResumenDeVentasPorPeriodo_fechaFinNula() {
        String fechaInicioStr = "2025-05-18";
        String fechaFinStr = null;

        Response response = compraAPI.obtenerResumenDeVentasPorPeriodo(1, securityContext, fechaInicioStr, fechaFinStr);

        assertEquals(400, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al procesar los parametros"));
        verify(servicioCompra, never()).resumenVentasPorPeriodo(anyInt(), any(), any());
    }

    // Tests para obtenerResumenDeVentasDiario

    @Test
    void testObtenerResumenDeVentasDiario_exito_estadoAprobada() {
        EstadoCompra estado = EstadoCompra.APROBADA;
        DTOResumenVentas resumenMock = mock(DTOResumenVentas.class);
        when(servicioCompra.resumenVentasPorEstado(1, estado)).thenReturn(resumenMock);

        Response response = compraAPI.obtenerResumenDeVentasDiario(1, securityContext, estado);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(resumenMock, response.getEntity());
        verify(servicioCompra).resumenVentasPorEstado(1, estado);
    }

    @Test
    void testObtenerResumenDeVentasDiario_exito_estadoRechazada() {
        EstadoCompra estado = EstadoCompra.RECHAZADA;
        DTOResumenVentas resumenMock = mock(DTOResumenVentas.class);
        when(servicioCompra.resumenVentasPorEstado(1, estado)).thenReturn(resumenMock);

        Response response = compraAPI.obtenerResumenDeVentasDiario(1, securityContext, estado);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(resumenMock, response.getEntity());
        verify(servicioCompra).resumenVentasPorEstado(1, estado);
    }

    @Test
    void testObtenerResumenDeVentasDiario_exito_estadoPendiente() {
        EstadoCompra estado = EstadoCompra.PENDIENTE;
        DTOResumenVentas resumenMock = mock(DTOResumenVentas.class);
        when(servicioCompra.resumenVentasPorEstado(1, estado)).thenReturn(resumenMock);

        Response response = compraAPI.obtenerResumenDeVentasDiario(1, securityContext, estado);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(resumenMock, response.getEntity());
        verify(servicioCompra).resumenVentasPorEstado(1, estado);
    }

    @Test
    void testObtenerResumenDeVentasDiario_errorResumen() {
        EstadoCompra estado = EstadoCompra.RECHAZADA;
        when(servicioCompra.resumenVentasPorEstado(1, estado)).thenReturn(null);

        Response response = compraAPI.obtenerResumenDeVentasDiario(1, securityContext, estado);

        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al generar el resumen"));
        verify(servicioCompra).resumenVentasPorEstado(1, estado);
    }

    @Test
    void testObtenerResumenDeVentasDiario_estadoNulo() {
        EstadoCompra estado = null;
        DTOResumenVentas resumenMock = mock(DTOResumenVentas.class);
        when(servicioCompra.resumenVentasPorEstado(1, estado)).thenReturn(resumenMock);

        Response response = compraAPI.obtenerResumenDeVentasDiario(1, securityContext, estado);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(resumenMock, response.getEntity());
        verify(servicioCompra).resumenVentasPorEstado(1, estado);
    }
}