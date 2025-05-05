package org.example.productcategory.service;

import org.example.productcategory.dto.CategoryParentTreeDto;
import org.example.productcategory.dto.CategoryRequestDto;
import org.example.productcategory.dto.CategoryResponseDto;
import org.example.productcategory.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
  Page<CategoryResponseDto> getAllCategories(Pageable pageable);

  CategoryResponseDto getCategoryById(Long id);

  Category getCategoryEntityById(Long id);

  CategoryResponseDto createCategory(CategoryRequestDto dto);

  CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto);

  CategoryParentTreeDto getParentTree(Long categoryId);

  void deleteCategory(Long id);
}