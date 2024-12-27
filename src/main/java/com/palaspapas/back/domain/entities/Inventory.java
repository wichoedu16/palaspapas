package com.palaspapas.back.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "inventario")
public class Inventory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventario-sequence")
    private Long id;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private String tipo;
    private int total;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime moveDate;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id")
    private Ingredient ingredient;

}