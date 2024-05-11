package com.example.shopping_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="products")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 350)
    private String name;

    private Float price;

    @Column(name = "thumbnail", length = 300)
    private String thumbnail;

    @Column(name = "description")
    private String description;


//    @Column(name="image_url",  length = 300)
//    private String imageUrl;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
