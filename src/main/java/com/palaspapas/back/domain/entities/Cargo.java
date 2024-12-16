package com.palaspapas.back.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cargo")
public class Cargo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cargo-sequence")
    private Long id;
    @NonNull
    private String descripcion;
    @Column(unique = true, nullable = false)
    private String codigo;
    @NonNull
    private String estado;
    @NonNull
    private Double salario;


    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

}
