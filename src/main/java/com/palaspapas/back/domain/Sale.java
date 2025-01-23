package com.palaspapas.back.domain;

import com.palaspapas.back.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale extends BaseDomain {
    private LocalDateTime saleDate;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;
    private String saleStatus;        // PENDING, COMPLETED, CANCELLED, CREDITED
    private String paymentType;       // CASH, CARD, CREDIT
    private String notes;
    private Long createdBy;
    private Long approvedBy;
    private List<SaleDetail> details = new ArrayList<>();
}