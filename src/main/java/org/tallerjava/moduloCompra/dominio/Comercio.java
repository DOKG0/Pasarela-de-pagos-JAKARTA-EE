package org.tallerjava.moduloCompra.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.tallerjava.moduloCompra.dominio.datatypes.DTOCompra;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
    @Column(unique = true)
    private String usuario;
    private double importeVentasDelDia; //to do: implementar un schedule que lo resetee a 0 todos los dias
    @Version
    private Integer version;

    @OneToMany(
        mappedBy = "comercio",
        orphanRemoval = true, 
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    private List<Pos> poses = new ArrayList<Pos>();

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
        resumen.setFechaInicio(fechaInicio.toString());
        resumen.setFechaFin(fechaFin.toString());

        List<DTOCompra> compras = new ArrayList<DTOCompra>();
        for (Compra c : this.compras) { //se incluyen las fechas de inicio y fin dentro del periodo
            if ((
                    c.getFecha().toLocalDate().isAfter(fechaInicio.toLocalDate()) ||
                    c.getFecha().toLocalDate().isEqual(fechaInicio.toLocalDate())
                ) && (
                    c.getFecha().toLocalDate().isBefore(fechaFin.toLocalDate()) ||
                    c.getFecha().toLocalDate().isEqual(fechaFin.toLocalDate())
                )) {
                compras.add(c.getDTOCompra());
                resumen.setMontoTotal(resumen.getMontoTotal() + c.getMonto());
            }
        }

        resumen.setCantidadVentas(compras.size());
        resumen.setVentas(compras);

        return resumen;
    }

    public Pos buscarPosPorId(Integer id) {
        for (Pos pos : this.poses) {
            if (pos.getId().equals(id)) {
                return pos;
            }
        }
        return null;
    }

    public Pos buscarPosPorIdentificador(String identificadorPos) {
        for (Pos pos : this.poses) {
            if (pos.getIdentificador().equals(identificadorPos)) {
                return pos;
            }
        }
        return null;
    }

    public void agregarPos(Pos pos) {
        this.poses.add(pos);
    }
}