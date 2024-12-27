package com.palaspapas.back.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "position")
public class Position implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "position-sequence")
    private Long id;
    @NonNull
    private String description;
    @Column(unique = true, nullable = false)
    private String code;
    @NonNull
    private String status;
    @NonNull
    private Double salary;

    @ManyToOne
    @JoinColumn(name = "departament_id")
    private Departament departament;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "position")
    private List<Employee> employees;

}
