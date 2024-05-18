package com.example.shopping_app.convert;

import com.example.shopping_app.dto.OrderDTO;
import com.example.shopping_app.dto.ProductDTO;
import com.example.shopping_app.entity.Order;
import com.example.shopping_app.entity.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProductConverter {
    private final TypeMap<ProductDTO, Product> typeMap;

    @Autowired
    public ProductConverter(ModelMapper modelMapper) {
        this.typeMap = modelMapper.createTypeMap(ProductDTO.class, Product.class);
        // Specify fields to skip
        this.typeMap.addMappings(mapper -> mapper.skip(Product::setId));
    }

    public Product toEntity (ProductDTO productDTO){
        Product product = new Product();
        this.typeMap.map(productDTO, product);
        return product;
    }



}
