package com.palaspapas.back.domain;

import com.palaspapas.back.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier extends BaseDomain {
    private String name;
    private String documentNumber;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private BigDecimal totalDebt;
    private List<SupplierPayment> payments;
}