package com.onejava.service;

import com.onejava.entity.Product;
import com.onejava.model.ProductDto;
import com.onejava.modelMapper.ModelMapper;
import com.onejava.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * Tests the ProductService.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductServiceTest {

    /**
     * The service that we want to test.
     */
    @Autowired
    private ProductService productService;

    /**
     * A mock version of the ProductRepository for use in our tests.
     */
    @MockBean
    private ProductRepository productRepository;

    @Test
    @DisplayName("Test findById Success")
    void testFindByIdSuccess() {
        // Setup our mock
        ProductDto mockProduct = new ProductDto(1L, "Product Name", 10, 1);
        doReturn(Optional.of(ModelMapper.convert(mockProduct, Product.class))).when(productRepository).findById(1L);

        // Execute the service call
        Optional<ProductDto> returnedProduct = productService.findById(1L);

        // Assert the response
        Assertions.assertTrue(returnedProduct.isPresent(), "Product was not found");
        //Assertions.assertSame(returnedProduct.get(), mockProduct, "Products should be the same");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        // Setup our mock
        ProductDto mockProduct = new ProductDto(1L, "Product Name", 10, 1);
        doReturn(Optional.empty()).when(productRepository).findById(1L);

        // Execute the service call
        Optional<ProductDto> returnedProduct = productService.findById(1L);

        // Assert the response
        Assertions.assertFalse(returnedProduct.isPresent(), "Product was found, when it shouldn't be");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        // Setup our mock
        ProductDto mockProduct = new ProductDto(1L, "Product Name", 10, 1);
        ProductDto mockProduct2 = new ProductDto(2L, "Product Name 2", 15, 3);
        doReturn(Arrays.asList(mockProduct, mockProduct2)).when(productRepository).findAll();

        // Execute the service call
        List<ProductDto> products = productService.findAll();

        Assertions.assertEquals(2, products.size(), "findAll should return 2 products");
    }

    @Test
    @DisplayName("Test save product")
    void testSave() {
        ProductDto mockProduct = new ProductDto(1L, "Product Name", 10, 1);
        doReturn(ModelMapper.convert(mockProduct, Product.class))
                .when(productRepository).save(any());

        ProductDto returnedProduct = productService.save(mockProduct);

        Assertions.assertNotNull(returnedProduct, "The saved product should not be null");
        Assertions.assertEquals(1, returnedProduct.getVersion().intValue(),
                "The version for a new product should be 1");
    }
}