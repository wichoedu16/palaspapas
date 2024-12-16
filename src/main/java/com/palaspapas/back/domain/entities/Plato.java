package com.palaspapas.back.domain.entities;

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
@Table(name = "plato")
public class Plato implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plato-sequence")
    private Long id;
    @Column(unique = true, nullable = false)
    private String nombre;
    @Column(nullable = false)
    private BigDecimal pvp;
    @Column(nullable = false)
    private String estado;
    @Column()
    private String promocion;
    @Column(name = "dia-promocion")
    private String diaPromocion;
}
