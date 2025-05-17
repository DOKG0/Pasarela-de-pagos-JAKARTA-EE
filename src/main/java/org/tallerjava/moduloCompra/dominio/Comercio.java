package org.tallerjava.moduloCompra.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.tallerjava.moduloCompra.dominio.datatypes.DTOCompra;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="comercio_MOD_COMPRA")
@Table(name="comercio_MOD_COMPRA")
public class Comercio {
    @Id
    private Integer id;
    private double importeVentasDelDia; //to do: implementar un schedule que lo resetee a 0 todos los dias

    @OneToMany(
        orphanRemoval = true, 
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    @JoinTable(name = "compras_comercio_MOD_COMPRA")
    private List<Compra> compras = new ArrayList<Compra>();
    
    @OneToOne(
        orphanRemoval = true, 
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    private CuentaBancoComercio cuentaBanco;

    public void agregarCompra(Compra compra) {
        compras.add(compra);
    }

    public DTOResumenVentas getResumenVentasPorEstado(EstadoCompra estado) {
        DTOResumenVentas resumen = new DTOResumenVentas();
        resumen.setIdComercio(this.id);
        resumen.setFechaInicio(null);
        resumen.setFechaFin(null);

        List<DTOCompra> compras = new ArrayList<DTOCompra>();
        for (Compra c : this.compras) {
            if (c.getEstado().equals(estado)) {
                compras.add(c.getDTOCompra());
                resumen.setMontoTotal(resumen.getMontoTotal() + c.getMonto());
            }
        }

        resumen.setCantidadVentas(compras.size());
        resumen.setVentas(compras);

        return resumen;
    }

    public DTOResumenVentas getResumenVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {

        DTOResumenVentas resumen = new DTOResumenVentas();
        resumen.setIdComercio(this.id);
        resumen.setFechaInicio(fechaInicio);
        resumen.setFechaFin(fechaFin);

        List<DTOCompra> compras = new ArrayList<DTOCompra>();
        for (Compra c : this.compras) {
            if (c.getFecha().isAfter(fechaInicio) && c.getFecha().isBefore(fechaFin)) {
                compras.add(c.getDTOCompra());
                resumen.setMontoTotal(resumen.getMontoTotal() + c.getMonto());
            }
        }
        resumen.setCantidadVentas(compras.size());
        resumen.setVentas(compras);

        return resumen;
    }
}