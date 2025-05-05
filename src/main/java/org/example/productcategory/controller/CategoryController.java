package org.example.productcategory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.productcategory.dto.CategoryParentTreeDto;
import org.example.productcategory.dto.CategoryRequestDto;
import org.example.productcategory.dto.CategoryResponseDto;
import org.example.productcategory.dto.PagedResponse;
import org.example.productcategory.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public PagedResponse<CategoryResponseDto> getAll(
          @RequestParam(defaultValue = "0", required = false) int page,
          @RequestParam(defaultValue = "10", required = false) int size,
          @RequestParam(defaultValue = "id,asc", required = false) String[] sort
  ) {
    Sort.Direction direction = Sort.Direction.fromString(sort[1]);
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

    Page<CategoryResponseDto> resultPage = categoryService.getAllCategories(pageable);
    return new PagedResponse<>(
            resultPage.getContent(),
            resultPage.getNumber(),
            resultPage.getSize(),
            resultPage.getTotalElements(),
            resultPage.getTotalPages(),
            resultPage.isLast()
    );
  }

  @GetMapping("/{id}")
  public CategoryResponseDto getById(@PathVariable Long id) {
    return categoryService.getCategoryById(id);
  }

  @GetMapping("/{id}/parents")
  public CategoryParentTreeDto getParentTree(@PathVariable Long id) {
    return categoryService.getParentTree(id);
  }

  @PostMapping
  public CategoryResponseDto create(@Valid @RequestBody CategoryRequestDto dto) {
    return categoryService.createCategory(dto);
  }

  @PutMapping("/{id}")
  public CategoryResponseDto update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto dto) {
    return categoryService.updateCategory(id, dto);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    categoryService.deleteCategory(id);
  }

}