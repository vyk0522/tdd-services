package com.onejava.service;

import com.onejava.model.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<ProductDto> findById(Long id);
    List<ProductDto> findAll();
    boolean update(ProductDto product);
    ProductDto save(ProductDto product);
    boolean delete(Long id);
}
