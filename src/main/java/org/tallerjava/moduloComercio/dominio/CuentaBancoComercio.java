package org.tallerjava.moduloComercio.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="cuenta_banco_comercio_MOD_COMERCIO")
@Table(name="cuenta_banco_comercio_MOD_COMERCIO")
public class CuentaBancoComercio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String numeroCuenta;
    private String banco;

    // Métodos de negocio
    public boolean esCuentaValida() {
        return numeroCuenta != null && !numeroCuenta.isEmpty() &&
               banco != null && !banco.isEmpty();
    }
}