package com.palaspapas.back.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "departamento")
public class Departamento implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departamento-sequence")
    private Long id;
    @Column(unique = true, nullable = false)
    private String description;
    @Column(unique = true, nullable = false)
    private String code;
    @Column(nullable = false)
    private String status;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "departamento")
    private List<Cargo> cargos;
}
