package kz.dara.test.qs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class ProductModel extends BaseModel {

    @Column(name = "model")
    private String model;

    @Column(name = "color")
    private String color;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Long price;

    @Column(name = "image")
    private String image;

    public String loadImage(){
        if (image == null || image.isEmpty())
            return "/storage/default.png";
        return image;
    }
}
