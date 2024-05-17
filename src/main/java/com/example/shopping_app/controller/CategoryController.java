package com.example.shopping_app.controller;


import com.example.shopping_app.dto.CategoryDTO;
import com.example.shopping_app.entity.Category;
import com.example.shopping_app.response.CategoryResponse;
import com.example.shopping_app.service.CategoryService;
import com.example.shopping_app.util.LocalizationUtil;
import com.example.shopping_app.util.MessageKeyUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final LocalizationUtil localizationUtil;

    @PostMapping("")
    //@RequestBody helps Spring convert JSON from request body into Java object.
    public ResponseEntity<?> createCategory(
            @Valid
            @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errMessage= result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errMessage);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert category successfully: " + categoryDTO.getName());
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategory() {
        List<Category> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete id: " + id +" success!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, HttpServletRequest request, @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        String success = localizationUtil.setLocaleMessage(MessageKeyUtil.UPDATE_CATEGORY_SUCCESS);
        return ResponseEntity.ok(CategoryResponse.builder()
                .message(success)
                .build());
    }


}
