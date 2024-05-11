package com.example.shopping_app.dto;

import jakarta.persistence.Column;
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
