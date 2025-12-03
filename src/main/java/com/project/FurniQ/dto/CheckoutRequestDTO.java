package com.project.FurniQ.dto;

import lombok.Data;

@Data
public class CheckoutRequestDTO {
    private Integer userId;
    private String username;
    private String email;
    private String mobileNumber;
    private String address;
    private String paymentStatus;
}