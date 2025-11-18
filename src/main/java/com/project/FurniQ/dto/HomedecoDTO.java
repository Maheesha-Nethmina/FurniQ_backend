package com.project.FurniQ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HomedecoDTO {
    private Integer id;
    private String decoName;
    private String decoDetails;
    private String decoPrice;
    private String decoSize;
    private Integer quantity;
    private String decoPicture;
}