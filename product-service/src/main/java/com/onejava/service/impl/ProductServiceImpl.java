package com.onejava.service.impl;

import com.onejava.constant.TypeReferenceConstant;
import com.onejava.entity.Product;
import com.onejava.model.ProductDto;
import com.onejava.modelMapper.ModelMapper;
import com.onejava.repository.ProductRepository;
import com.onejava.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Optional<ProductDto> findById(Long id) {
        log.info("Find product with id: {}", id);
        Optional<Product> product = productRepository.findById(id);
        return ModelMapper
                .convert(product, TypeReferenceConstant.OPTIONAL_OF_PRODUCT_DTO_TYPE_REFERENCE);
    }

    @Override
    public List<ProductDto> findAll() {
        log.info("Find all products");
        List<Product> guests = this.productRepository.findAll();
        return ModelMapper
                .convert(guests, TypeReferenceConstant.LIST_OF_PRODUCT_DTO_TYPE_REFERENCE);
    }

    @Override
    public boolean update(ProductDto productDto) {
        log.info("Update product: {}", productDto);
        Product product = ModelMapper.convert(productDto, Product.class);
        productRepository.save(product);
        return true;
    }

    @Override
    public ProductDto save(ProductDto newProduct) {
        newProduct.setVersion(1);
        log.info("Save product to the database: {}", newProduct);
        Product product = this.productRepository.save(ModelMapper.convert(newProduct, Product.class));
        return ModelMapper.convert(product, ProductDto.class);
    }

    @Override
    public boolean delete(Long id) {
        log.info("Delete product with id: {}", id);
        productRepository.deleteById(id);
        return true;
    }
}
