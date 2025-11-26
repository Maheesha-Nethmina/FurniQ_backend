package com.project.FurniQ.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "oders")
public class Oder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private String OderType;
    private Double price;
    private String username;
    private String productName;
    @Column(columnDefinition = "TEXT")
    private String address;
    @Column(length = 10)
    private String mobileNumber;
    private String email;

}
