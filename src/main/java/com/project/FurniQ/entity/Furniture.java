package com.project.FurniQ.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name= "furniture")
@Builder
public class Furniture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String furnitureName;
    private String furnitureDetails;
    private String furnitureType;
    private String furniturePrice;
    private String furnitureSize;
    private Integer quantity;
    private String furniturePicture;
}