package com.palaspapas.back.service.interfaces;

import com.palaspapas.back.domain.Product;
import java.util.List;

public interface IProductService {
    Product create(Product product);
    Product update(Long id, Product product);
    Product findById(Long id);
    List<Product> findAll();
    List<Product> findByCategoryId(Long categoryId);
    void delete(Long id);
    Product changeStatus(Long id, Boolean status);
    boolean hasAvailableStock(Long productId);
}