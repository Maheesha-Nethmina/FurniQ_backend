package com.project.FurniQ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OderDTO {

    private Integer orderId;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private String OderType;
    private Double price;
    private String username;
    private String productName;
    private String address;
    private String mobileNumber;
    private String email;
    private String OderStatus;
    private String PaymentStatus;

}
