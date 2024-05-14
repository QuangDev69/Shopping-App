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
    private Float price;
    private int numberOfProducts;
    private Float totalMoney;
    private String color;

}
