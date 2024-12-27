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
@Table(name = "provider")
public class Provider implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provider-sequence")
    private Long id;
    @Column(unique = true, nullable = false)
    private String empresa;
    @Column(nullable = false)
    private String nombreProveedor;
    @Column(nullable = false)
    private String telefonoProveedor;

    private String banco;
    @Column(name = "tipo_cuenta")
    private String tipoCuenta;
    @Column(name = "numero_cuenta", nullable = false)
    private String numeroCuenta;
    private String identificacion;
    @Column(name = "nombre_contacto", nullable = false)
    private String nombreContacto;
    @Column(name = "telefono_contacto", nullable = false)
    private String telefonoContacto;
    @Column(name = "correo_contacto", nullable = false)
    private String correoContacto;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "provider")
    private List<Ingredient> ingredients;
}