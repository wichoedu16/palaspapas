package com.palaspapas.back.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ingrediente")
public class Ingrediente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingrediente-sequence")
    private Long id;
    @Column(unique = true, nullable = false)
    private String nombre;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private String unidad;
    @Column(nullable = false)
    private BigDecimal costo;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
    @Column(name = "proveedor_id", insertable = false, updatable = false)
    private Long proveedorId;
}
