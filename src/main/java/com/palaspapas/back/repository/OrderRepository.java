package com.palaspapas.back.repository;

import com.palaspapas.back.model.entity.sale.Order;
import com.palaspapas.back.model.entity.sale.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);

    @Query("SELECT o FROM Order o WHERE " +
            "o.status = :status AND o.active = true")
    List<Order> findByStatus(@Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o WHERE " +
            "o.orderDate BETWEEN :startDate AND :endDate AND o.active = true")
    List<Order> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT o FROM Order o WHERE " +
            "LOWER(o.customerName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "o.customerPhone LIKE CONCAT('%', :searchTerm, '%') OR " +
            "o.orderNumber LIKE CONCAT('%', :searchTerm, '%')")
    Page<Order> search(@Param("searchTerm") String searchTerm, Pageable pageable);
}
