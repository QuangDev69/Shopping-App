package com.example.shopping_app.service;

import com.example.shopping_app.dto.CategoryDTO;
import com.example.shopping_app.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(CategoryDTO category);

    Category getCategoryById(long id);

    List<Category> getAllCategory();

    Category updateCategory(long id, CategoryDTO category);

    void deleteCategory(long id);
}
