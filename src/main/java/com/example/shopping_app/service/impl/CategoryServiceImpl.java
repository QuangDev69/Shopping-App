package com.example.shopping_app.service.impl;

import com.example.shopping_app.dto.CategoryDTO;
import com.example.shopping_app.model.Category;
import com.example.shopping_app.repository.CategoryRepository;
import com.example.shopping_app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder().name(categoryDTO.getName()).build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long id, CategoryDTO categoryDTO) {
        Category existCategory = getCategoryById(id);
        existCategory.setName(categoryDTO.getName());
        categoryRepository.save(existCategory);
        return existCategory;
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
