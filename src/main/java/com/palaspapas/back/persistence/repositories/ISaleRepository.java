package com.palaspapas.back.persistence.repositories;

import com.palaspapas.back.persistence.entities.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ISaleRepository extends JpaRepository<SaleEntity, Long> {

    @Query("SELECT s FROM SaleEntity s " +
            "LEFT JOIN FETCH s.details d " +
            "LEFT JOIN FETCH d.product " +
            "LEFT JOIN FETCH d.ingredient " +
            "WHERE s.id = :id")
    Optional<SaleEntity> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT s FROM SaleEntity s " +
            "WHERE s.saleDate BETWEEN :startDate AND :endDate")
    List<SaleEntity> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<SaleEntity> findBySaleStatus(String status);

    @Query("SELECT s FROM SaleEntity s " +
            "WHERE s.saleDate >= :date " +
            "AND s.saleStatus = 'COMPLETED'")
    List<SaleEntity> findCompletedSalesFromDate(@Param("date") LocalDateTime date);

    List<SaleEntity> findByCreatedBy(Long userId);
}