package com.project.FurniQ.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.net.URL;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "homedeco")
@Builder

public class HomeDeco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String decoName;
    private String decoDetails;
    private String decoPrice;
    private String decoSize;
    private Integer quantity;
    private URL decoPicture;
}
