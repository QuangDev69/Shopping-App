package com.example.shopping_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="product_images")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Chỉ định fetch type là LAZY
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url", length = 300)
    private String imageUrl;


}
