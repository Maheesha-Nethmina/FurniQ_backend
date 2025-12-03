package com.project.FurniQ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Integer cartId;
    private Integer productId;
    private String productType;
    private Integer quantity;
    private String productName;
    private Double price;
    private String image;
    private Integer maxStock;
}