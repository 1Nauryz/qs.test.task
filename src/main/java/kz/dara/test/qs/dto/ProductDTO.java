package kz.dara.test.qs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

    private Long id;
    private String model;
    private String color;
    private String description;
    private int price;
    private String image;
}
