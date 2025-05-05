package org.example.productcategory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.productcategory.dto.CategoryParentTreeDto;
import org.example.productcategory.dto.CategoryRequestDto;
import org.example.productcategory.dto.CategoryResponseDto;
import org.example.productcategory.entity.Category;
import org.example.productcategory.exception.BadRequestException;
import org.example.productcategory.exception.CategoryNotFoundException;
import org.example.productcategory.mapper.CategoryMapper;
import org.example.productcategory.mapper.CategoryParentTreeMapper;
import org.example.productcategory.repository.CategoryRepository;
import org.example.productcategory.repository.ProductRepository;
import org.example.productcategory.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;
  private final ProductRepository productRepository;
  private final CategoryParentTreeMapper categoryParentTreeMapper;

  @Override
  public Page<CategoryResponseDto> getAllCategories(Pageable pageable) {
    log.info("Fetching all categories with pagination: {}", pageable);
    return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
  }

  @Override
  public CategoryResponseDto getCategoryById(Long id) {
    log.info("Fetching category by ID: {}", id);
    return categoryMapper.toDto(findCategoryById(id));
  }

  @Override
  public CategoryParentTreeDto getParentTree(Long categoryId) {
    log.info("Fetching parent tree for category ID: {}", categoryId);
    Category category = findCategoryById(categoryId);
    return categoryParentTreeMapper.toDtoRecursive(category);
  }

  @Override
  public Category getCategoryEntityById(Long id) {
    log.debug("Fetching raw category entity by ID: {}", id);
    return findCategoryById(id);
  }

  @Override
  @Transactional
  public CategoryResponseDto createCategory(CategoryRequestDto dto) {
    log.info("Creating category: {}", dto);

    validateCategoryNameUniqueness(dto.getName());

    Category category = categoryMapper.toEntity(dto);
    if (dto.getParentId() != null) {
      category.setParent(findCategoryById(dto.getParentId()));
    }

    Category saved = categoryRepository.save(category);
    log.info("Category created with ID: {}", saved.getId());
    return categoryMapper.toDto(saved);
  }

  @Override
  @Transactional
  public CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto) {
    log.info("Updating category ID: {} with data: {}", id, dto);

    Category existing = findCategoryById(id);
    validateCategoryNameUniqueness(dto.getName(), id);

    existing.setName(dto.getName());
    existing.setParent(dto.getParentId() != null ? findCategoryById(dto.getParentId()) : null);

    Category updated = categoryRepository.save(existing);
    log.info("Updated category ID: {}", updated.getId());
    return categoryMapper.toDto(updated);
  }

  @Override
  @Transactional
  public void deleteCategory(Long id) {
    log.warn("Attempting to delete category ID: {}", id);

    Category category = findCategoryById(id);

    if (categoryRepository.existsByParentId(id)) {
      throw new BadRequestException("Cannot delete category with child categories.");
    }

    int productCount = productRepository.countProductsByCategoryId(id);
    if (productCount > 0) {
      log.error("Category ID {} has {} assigned products", id, productCount);
      throw new BadRequestException("Cannot delete category with assigned products.");
    }

    categoryRepository.delete(category);
    log.info("Deleted category ID: {}", id);
  }

  private Category findCategoryById(Long id) {
    return categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException(id));
  }

  private void validateCategoryNameUniqueness(String name) {
    if (categoryRepository.existsByName(name)) {
      throw new BadRequestException("Category with name '" + name + "' already exists.");
    }
  }

  private void validateCategoryNameUniqueness(String name, Long currentCategoryId) {
    boolean exists = categoryRepository.existsByName(name);
    Category existing = categoryRepository.findById(currentCategoryId).orElse(null);

    if (exists && (existing == null || !existing.getName().equals(name))) {
      throw new BadRequestException("Category with name '" + name + "' already exists.");
    }
  }
}