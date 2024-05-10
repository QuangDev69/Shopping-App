package com.example.shopping_app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

    private Long orderId;
    private Long productId;
    private Long price;
    private int numberOfProducts;
    private int totalMoney;
    private String color;

}
