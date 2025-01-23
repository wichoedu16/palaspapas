package com.palaspapas.back.domain;

import com.palaspapas.back.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierPayment extends BaseDomain {
    private Supplier supplier;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String documentNumber;
    private String referenceNumber;
    private String paymentType;  // CASH, TRANSFER, CHECK
    private String notes;
    private String statusPayment;      // PENDING, APPROVED, REJECTED
    private Boolean isPartialPayment;
    private Long createdBy;
    private Long approvedBy;
}