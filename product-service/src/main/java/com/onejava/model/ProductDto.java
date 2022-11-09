package com.onejava.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProductDto {
    private Long id;
    private String name;
    private Integer quantity;
    private Integer version;

    public ProductDto(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public ProductDto(long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
}
