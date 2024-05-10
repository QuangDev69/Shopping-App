package com.example.shopping_app.dto;

import com.example.shopping_app.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {

    private Long productId;

    @Column(name = "image_url", length = 300)
    private String imageUrl;

}
