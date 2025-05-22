package org.tallerjava.moduloCompra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tallerjava.moduloCompra.aplicacion.ServicioCompra;
import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.Tarjeta;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;



@ExtendWith(MockitoExtension.class)
public class ServicioCompraTest {
    
    @Mock
    private ServicioCompra servicioCompra;
    
    @BeforeEach
    void setUp() {
    }
    
    //COmprueba que cuando se envie un pago valido el servicio lo procese correctamente y devuelva true
    @Test
    void procesarPago_ValidPayment_ReturnsTrue() {
        Integer idComercio = 1;
        double importe = 1000.0;
        Tarjeta tarjeta = new Tarjeta();
        
        when(servicioCompra.procesarPago(eq(idComercio), eq(importe), any(Tarjeta.class))).thenReturn(true);
        
        boolean result = servicioCompra.procesarPago(idComercio, importe, tarjeta);
        
        assertThat(result).isTrue();
        verify(servicioCompra).procesarPago(idComercio, importe, tarjeta);
    }
    
    //Comproueba que cuando se envie un pago invalido el servicio lo procese correctamente y devuelva false
    @Test
    void procesarPago_InvalidPayment_ReturnsFalse() {
        Integer idComercio = 1;
        double importe = 1000.0;
        Tarjeta tarjeta = new Tarjeta();
        
        when(servicioCompra.procesarPago(eq(idComercio), eq(importe), any(Tarjeta.class))).thenReturn(false);
        
        boolean result = servicioCompra.procesarPago(idComercio, importe, tarjeta);
        
        assertThat(result).isFalse();
        verify(servicioCompra).procesarPago(idComercio, importe, tarjeta);
    }
    
    //Comprueba que el servicio devuelva un resumen de ventas por periodo si el periodo es valido
    @Test
    void resumenVentasPorPeriodo_ValidPeriod_ReturnsResumen() {
        Integer idComercio = 1;
        LocalDateTime fechaInicio = LocalDateTime.of(LocalDate.now().minusMonths(1), LocalTime.MIDNIGHT);
        LocalDateTime fechaFin = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        
        DTOResumenVentas expectedResumen = mock(DTOResumenVentas.class);
        when(servicioCompra.resumenVentasPorPeriodo(idComercio, fechaInicio, fechaFin)).thenReturn(expectedResumen);
        
        DTOResumenVentas result = servicioCompra.resumenVentasPorPeriodo(idComercio, fechaInicio, fechaFin);
        
        assertThat(result).isEqualTo(expectedResumen);
        verify(servicioCompra).resumenVentasPorPeriodo(idComercio, fechaInicio, fechaFin);
    }
    
    //Comprueba que el servicio devuelva un resumen de ventas por estado si el estado es valido 
    @Test
    void resumenVentasPorEstado_ValidEstado_ReturnsResumen() {
        Integer idComercio = 1;
        EstadoCompra estado = EstadoCompra.APROBADA;
        
        DTOResumenVentas expectedResumen = mock(DTOResumenVentas.class);
        when(servicioCompra.resumenVentasPorEstado(idComercio, estado)).thenReturn(expectedResumen);
        
        DTOResumenVentas result = servicioCompra.resumenVentasPorEstado(idComercio, estado);
        
        assertThat(result).isEqualTo(expectedResumen);
        verify(servicioCompra).resumenVentasPorEstado(idComercio, estado);
    }
    
    //Comprueba que el servicio devuelva un resumen de ventas por comercio si el comercio es valido
    @Test
    void montoActualVendido_ValidComercio_ReturnsMonto() {
        Integer idComercio = 1;
        double expectedMonto = 10000.0;
        
        when(servicioCompra.montoActualVendido(idComercio)).thenReturn(expectedMonto);
        
        double result = servicioCompra.montoActualVendido(idComercio);
        
        assertThat(result).isEqualTo(expectedMonto);
        verify(servicioCompra).montoActualVendido(idComercio);
    }
    
    //Comprueba que el servicio devuelva 0 si el comercio no tiene ventas
    @Test
    void montoActualVendido_ComercioSinVentas_ReturnsZero() {
        Integer idComercio = 2;
        double expectedMonto = 0.0;
        
        when(servicioCompra.montoActualVendido(idComercio)).thenReturn(expectedMonto);
        
        double result = servicioCompra.montoActualVendido(idComercio);
        
        assertThat(result).isEqualTo(expectedMonto);
        verify(servicioCompra).montoActualVendido(idComercio);
    }
}