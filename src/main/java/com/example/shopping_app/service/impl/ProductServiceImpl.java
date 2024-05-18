package com.example.shopping_app.service.impl;

import com.example.shopping_app.Exceptional.DataNotFoundException;
import com.example.shopping_app.Exceptional.InvalidParamException;
import com.example.shopping_app.convert.ProductConverter;
import com.example.shopping_app.dto.ProductDTO;
import com.example.shopping_app.dto.ProductImageDTO;
import com.example.shopping_app.entity.Category;
import com.example.shopping_app.entity.Product;
import com.example.shopping_app.entity.ProductImage;
import com.example.shopping_app.repository.CategoryRepository;
import com.example.shopping_app.repository.ProductImageRepository;
import com.example.shopping_app.repository.ProductRepository;
import com.example.shopping_app.response.ProductResponse;
import com.example.shopping_app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductConverter productConverter;

    @Override
    public Product createProduct(ProductDTO productDTO)  {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Can not find Category with Id " + productDTO.getCategoryId()));

        Product newProduct = productConverter.toEntity(productDTO);
        newProduct.setCategory(existingCategory);
//        Product newProduct = Product.builder()
//                .name(productDTO.getName())
//                .price(productDTO.getPrice())
//                .thumbnail(productDTO.getThumbnail())
//                .category(existingCategory)
//                .build();
        productRepository.save(newProduct);
        return newProduct;
    }

    @Override
    public Product getProductById(long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Can not find Product with id " + productId));
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword,
                                                Long categoryId, PageRequest pageRequest) {
        // Lấy danh sách sản phẩm theo trang (page), giới hạn (limit), và categoryId (nếu có)
        Page<Product> productsPage;
        productsPage = productRepository.searchProducts(categoryId, keyword, pageRequest);
        return productsPage.map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO)  {

        Product existingProduct = getProductById(id);
        if (existingProduct != null ) {
            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("Can not find Category with Id " + productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(id);


        if (!optionalProduct.isPresent()) {
            throw new Exception("Product Not Found with id: " + id);
        }
        //        if(optionalProduct.isPresent()){
//            productRepository.delete( optionalProduct.get());
//
//        }
        optionalProduct.ifPresent(productRepository::delete);

    }

    @Override
    public boolean existByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
        // Kiểm tra sản phẩm tồn tại dựa trên productId được cung cấp
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found with ID: " + productId));

        // Kiểm tra số lượng hình ảnh hiện tại của sản phẩm
        if (productImageRepository.findByProductId(productId).size() >= 5) {
            throw new InvalidParamException("Number of images must be less than 5");
        }

        // Tạo mới đối tượng ProductImage
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        return productImageRepository.save(newProductImage);
    }

}
