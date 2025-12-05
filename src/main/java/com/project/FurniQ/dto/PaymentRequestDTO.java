package com.project.FurniQ.dto;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long amount;
    private String currency;
}