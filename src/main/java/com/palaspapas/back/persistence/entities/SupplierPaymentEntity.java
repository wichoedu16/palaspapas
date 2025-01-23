package com.palaspapas.back.persistence.entities;

import com.palaspapas.back.persistence.entities.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierPaymentEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private SupplierEntity supplier;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "document_number", length = 50)
    private String documentNumber;

    @Column(name = "reference_number", length = 50)
    private String referenceNumber;

    @Column(name = "payment_type", nullable = false, length = 20)
    private String paymentType;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false, length = 20)
    private String statusPayment;

    @Column(name = "is_partial_payment", nullable = false)
    private Boolean isPartialPayment;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "approved_by")
    private Long approvedBy;
}