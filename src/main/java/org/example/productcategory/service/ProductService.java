package org.example.productcategory.service;

import org.example.productcategory.dto.ProductRequestDto;
import org.example.productcategory.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
  Page<ProductResponseDto> getAllProducts(Pageable pageable, List<String> currencies);

  ProductResponseDto getProductById(Long id, List<String> currencies);

  ProductResponseDto createProduct(ProductRequestDto request);

  ProductResponseDto updateProduct(Long id, ProductRequestDto request);

  void softDeleteProduct(Long id);
}