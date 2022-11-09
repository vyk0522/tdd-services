package com.onejava.constant;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onejava.model.ProductDto;

import java.util.List;
import java.util.Optional;

public class TypeReferenceConstant {
    public static final TypeReference<List<ProductDto>> LIST_OF_PRODUCT_DTO_TYPE_REFERENCE = new TypeReference<>() {};
    public static final TypeReference<Optional<ProductDto>> OPTIONAL_OF_PRODUCT_DTO_TYPE_REFERENCE = new TypeReference<>() {};
}
