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
//    private String password;
    private String email;
    private String mobileNumber;
    private String role;

}
