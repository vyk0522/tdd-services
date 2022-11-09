package com.onejava.controller;

import com.onejava.model.ProductDto;
import com.onejava.modelMapper.ModelMapper;
import com.onejava.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc // it creates MockMvc and autoconfigures it
class ProductControllerTest {
    @MockBean
    private ProductService productService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /product/1 - Found")
    void testGetProductByIdFound() throws Exception{
        ProductDto mockProduct = new ProductDto(1L, "Apple TV", 10, 1);
        when(productService.findById(1L)).thenReturn(Optional.of(mockProduct));

        mockMvc.perform(get("/product/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/product/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Apple TV")))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("GET /product/1 - Not Found")
    void testGetProductByIdNotFound() throws Exception {
        // Set up mocked service
        doReturn(Optional.empty()).when(productService).findById(1L);

        // Execute the GET request
        mockMvc.perform(get("/product/{id}", 1))
                .andExpect(status().isNotFound()); // Validate that we get a 404 Not Found response
    }

    @Test
    @DisplayName("POST /product - Success")
    void testCreateProduct() throws Exception {
        // Setup mocked service
        ProductDto postProduct = new ProductDto("Product Name", 10);
        ProductDto mockProduct = new ProductDto(1L, "Product Name", 10, 1);
        doReturn(mockProduct).when(productService).save(any());

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ModelMapper.convert(postProduct)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/product/1"))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Product Name")))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("PUT /product/1 - Success")
    void testProductPutSuccess() throws Exception {
        // Setup mocked service
        ProductDto putProduct = new ProductDto("Product Name", 10);
        ProductDto mockProduct = new ProductDto(1L, "Product Name", 10, 1);
        doReturn(Optional.of(mockProduct)).when(productService).findById(1L);
        doReturn(true).when(productService).update(any());

        mockMvc.perform(put("/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(ModelMapper.convert(putProduct)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/product/1"))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Product Name")))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.version", is(2)));
    }

    @Test
    @DisplayName("PUT /product/1 - Version Mismatch")
    void testProductPutVersionMismatch() throws Exception {
        // Setup mocked service
        ProductDto putProduct = new ProductDto("Product Name", 10);
        ProductDto mockProduct = new ProductDto(1L, "Product Name", 10, 2);
        doReturn(Optional.of(mockProduct)).when(productService).findById(1L);
        doReturn(true).when(productService).update(any());

        mockMvc.perform(put("/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(ModelMapper.convert(putProduct)))

                // Validate the response code and content type
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /product/1 - Not Found")
    void testProductPutNotFound() throws Exception {
        // Setup mocked service
        ProductDto putProduct = new ProductDto("Product Name", 10);
        doReturn(Optional.empty()).when(productService).findById(1L);

        mockMvc.perform(put("/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(ModelMapper.convert(putProduct)))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /product/1 - Success")
    void testProductDeleteSuccess() throws Exception {
        // Set up mocked product
        ProductDto mockProduct = new ProductDto(1L, "Product Name", 10, 1);

        // Set up the mocked service
        doReturn(Optional.of(mockProduct)).when(productService).findById(1L);
        doReturn(true).when(productService).delete(1L);

        // Execute our DELETE request
        mockMvc.perform(delete("/product/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /product/1 - Not Found")
    void testProductDeleteNotFound() throws Exception {
        // Set up the mocked service
        doReturn(Optional.empty()).when(productService).findById(1L);

        // Execute our DELETE request
        mockMvc.perform(delete("/product/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /product/1 - Failure")
    void testProductDeleteFailure() throws Exception {
        // Set up mocked product
        ProductDto mockProduct = new ProductDto(1L, "Product Name", 10, 1);

        // Set up the mocked service
        doReturn(Optional.of(mockProduct)).when(productService).findById(1L);
        doReturn(false).when(productService).delete(1L);

        // Execute our DELETE request
        mockMvc.perform(delete("/product/{id}", 1))
                .andExpect(status().isInternalServerError());
    }

}
