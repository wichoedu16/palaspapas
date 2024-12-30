package com.palaspapas.back.repository.provider;

import com.palaspapas.back.model.entity.provider.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    List<Provider> findByActiveTrue();

    Optional<Provider> findByTaxIdAndActiveTrue(String taxId);

    boolean existsByTaxId(String taxId);

    @Query("SELECT p FROM Provider p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.taxId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND " +
            "p.active = true")
    Page<Provider> search(@Param("searchTerm") String searchTerm, Pageable pageable);
}