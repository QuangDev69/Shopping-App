package com.example.shopping_app.controller;


import com.example.shopping_app.dto.ProductDTO;
import jakarta.validation.Valid;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("products")

public class ProductController {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @GetMapping("")
    public ResponseEntity<String> getAllProduct(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return ResponseEntity.ok(String.format("Products!... page = %d, limit = %d", page, limit));
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@RequestBody helps Spring convert JSON from request body into Java object.
    public ResponseEntity<?> insertProduct(
            @Valid
            @ModelAttribute ProductDTO productDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errMessage= result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errMessage);
            }
            List<MultipartFile> files = productDTO.getFiles();
            for(MultipartFile file : files){
                    if (file.getSize() > MAX_FILE_SIZE) {
                        return ResponseEntity.badRequest().body("File size should not exceed 10MB");
                    }
                    String contentType = file.getContentType();
                    if(contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .body("File must be an image");
                    }
                    String filename = storeFile(file);
                    System.out.println("filename: "+filename);
            }
            return ResponseEntity.ok("insert product successfully" );
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    private String storeFile(MultipartFile file ) throws IOException {
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


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok("Delete id: " + id);
    }
}