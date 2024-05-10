package com.example.shopping_app.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ProductDTO {
    @NotBlank(message = "Can not be empty")
    @Size(min = 3, max = 200, message = "Must be between 3 to 200 char")
    private String name;

    @Min(value = 0, message = "Price more than 0")
    @Max(value = 10000, message = "Price less than 10000")
    private float price;
    private String thumbnail;
    private String description;
    private Long categoryId;
    private List<MultipartFile> files;
}
