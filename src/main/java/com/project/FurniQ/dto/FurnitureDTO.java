package com.project.FurniQ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FurnitureDTO {
    private Integer id;
    private String furnitureName;
    private String furnitureDetails;
    private String furnitureType;
    private String furniturePrice;
    private String furnitureSize;
    private Integer quantity;
    private URL furniturePicture;
}
