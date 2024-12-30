package com.palaspapas.back.repository.provider;

import com.palaspapas.back.model.entity.provider.PaymentStatus;
import com.palaspapas.back.model.entity.provider.ProviderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProviderPaymentRepository extends JpaRepository<ProviderPayment, Long> {
    List<ProviderPayment> findByProviderId(Long providerId);

    @Query("SELECT pp FROM ProviderPayment pp WHERE pp.provider.id = :providerId " +
            "AND pp.paymentDate BETWEEN :startDate AND :endDate")
    List<ProviderPayment> findByProviderIdAndDateRange(
            @Param("providerId") Long providerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT pp FROM ProviderPayment pp WHERE pp.status = :status")
    List<ProviderPayment> findByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT SUM(pp.totalAmount) FROM ProviderPayment pp WHERE " +
            "pp.provider.id = :providerId AND pp.status != 'COMPLETED'")
    BigDecimal findTotalPendingAmount(@Param("providerId") Long providerId);
}