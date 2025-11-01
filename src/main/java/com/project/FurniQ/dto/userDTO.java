package com.project.FurniQ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class userDTO {
    private Integer id;
    private String username;
    private String email;
    private String mobileNumber;
}
