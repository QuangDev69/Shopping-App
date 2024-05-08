package com.example.shopping_app.controller;


import com.example.shopping_app.dto.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {
    @GetMapping("")
    public ResponseEntity<String> getAllCategory(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return ResponseEntity.ok(String.format("hello!... page = %d, limit = %d", page, limit));
    }

    @PostMapping("")
    //@RequestBody helps Spring convert JSON from request body into Java object.
    public ResponseEntity<?> insertCategory(
            @Valid
            @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errMessage= result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errMessage);
        }
        return ResponseEntity.ok("insert categories " + categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok("Delete id: " + id);
    }


}
