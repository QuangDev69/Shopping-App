package com.example.shopping_app.response;

import com.example.shopping_app.entity.Order;
import com.example.shopping_app.entity.OrderDetail;
import com.example.shopping_app.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {

    private Long id;
    private Long orderId;
    private Long productId;
    private Float price;
    private int numberOfProducts;
    private Float totalMoney;
    private String color;


}
