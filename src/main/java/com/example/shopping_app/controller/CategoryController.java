package com.example.shopping_app.controller;


import com.example.shopping_app.dto.CategoryDTO;
import com.example.shopping_app.model.Category;
import com.example.shopping_app.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;


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
    public ResponseEntity<List<Category>> getAllCategory(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        List<Category> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete id: " + id +" success!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("OK");
    }


}
