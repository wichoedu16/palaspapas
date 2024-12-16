package com.palaspapas.back.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "preparacion")
public class Preparacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "preparacion-sequence")
    private Long id;
    @Column(nullable = false)
    private String nombreProducto;
    @Column(nullable = false)
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;
    @Column(name = "ingrediente_id", insertable = false, updatable = false)
    private Long ingredienteId;

    @ManyToOne
    @JoinColumn(name = "plato_id")
    private Plato plato;
    @Column(name = "plato_id", insertable = false, updatable = false)
    private Long platoId;
}
