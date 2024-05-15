package com.example.shopping_app.controller;


import com.example.shopping_app.dto.ProductDTO;
import com.example.shopping_app.dto.ProductImageDTO;
import com.example.shopping_app.entity.Product;
import com.example.shopping_app.Exceptional.InvalidParamException;
import com.example.shopping_app.service.ProductService;
import com.example.shopping_app.util.LocalizationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final LocalizationUtil localizationUtil;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllProduct(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {

        // Tạo đối tượng PageRequest với sắp xếp theo trường 'createdAt' giảm dần.
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        // Lấy trang sản phẩm từ productService.
        Page<Product> productPage = productService.getAllProducts(pageRequest);

        // Tạo map để chứa thông tin sản phẩm và thông tin phân trang.
        Map<String, Object> response = new HashMap<>();

        //GetContent() trả về một List<T> chứa các đối tượng trong trang hiện tại.
        response.put("product", productPage.getContent());
        response.put("currentPage", page);
        response.put("totalItem", productPage.getTotalElements());
        response.put("totalPage", productPage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@RequestBody helps Spring convert JSON from request body into Java object.
    public ResponseEntity<?> insertProduct(
            @Valid
            @ModelAttribute ProductDTO productDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errMessage);
            }
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() <= 5) {
                Product newProduct = productService.createProduct(productDTO);
                for (MultipartFile file : files) {
                    if (file.getSize() == 0) {
                        continue;
                    }
                    if (file.getSize() > MAX_FILE_SIZE) {
                        return ResponseEntity.badRequest().body("File size should not exceed 10MB");
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .body("File must be an image");
                    }
                    String filename = storeFile(file);
                    productService.createProductImage(
                            newProduct.getId(),
                            ProductImageDTO.builder().imageUrl(filename).build()
                    );
                }
                return ResponseEntity.ok("insert product successfully");
            } else throw new InvalidParamException("Number of images must be less than 5");


        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        // Lấy tên file gốc từ đối tượng file được gửi lên và làm sạch nó để tránh các ký tự đường dẫn không hợp lệ
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        // Tạo tên file duy nhất bằng cách kết hợp UUID với tên file gốc để tránh trùng lặp tên file
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;

        // Đặt đường dẫn thư mục upload tới một thư mục cụ thể trên máy
        Path uploadDir = Paths.get("C:/Users/Admin/Documents/Shopping-App/src/main/resources/uploads");

        // Kiểm tra xem thư mục upload đã tồn tại hay chưa, nếu chưa thì tạo mới
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir); // Tạo thư mục
        }
        // Tạo đối tượng Path cho file đích để lưu file được upload
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);

        // Sao chép file từ luồng nhập của file đến đích, ghi đè file nếu đã tồn tại
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        // Trả về tên file duy nhất để có thể được sử dụng sau này
        return uniqueFilename;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long productId) {

        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(product);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Oke");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
