package com.palaspapas.back.persistence.entities;

import com.palaspapas.back.persistence.entities.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suppliers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "document_number", length = 20)
    private String documentNumber;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 200)
    private String address;

    @Column(name = "total_debt", precision = 10, scale = 2)
    private BigDecimal totalDebt;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<SupplierPaymentEntity> payments = new ArrayList<>();
}