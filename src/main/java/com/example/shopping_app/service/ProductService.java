package com.example.shopping_app.service;

import com.example.shopping_app.Exception.DataNotFoundException;
import com.example.shopping_app.controller.ProductController;
import com.example.shopping_app.dto.ProductDTO;
import com.example.shopping_app.dto.ProductImageDTO;
import com.example.shopping_app.model.Product;
import com.example.shopping_app.model.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(long id) throws DataNotFoundException;

    Page<Product> getAllProducts(PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(long id);

    boolean existByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;
}
