package org.example.productcategory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.productcategory.dto.PagedResponse;
import org.example.productcategory.dto.ProductRequestDto;
import org.example.productcategory.dto.ProductResponseDto;
import org.example.productcategory.service.ProductService;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public PagedResponse<ProductResponseDto> getAll(
          @RequestParam(defaultValue = "0", required = false) int page,
          @RequestParam(defaultValue = "10", required = false) int size,
          @RequestParam(defaultValue = "id,asc", required = false) String[] sort,
          @RequestParam(defaultValue = "EUR", required = false) List<String> currencies
  ) {
    Sort.Direction direction = Sort.Direction.fromString(sort[1]);
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
    log.info("currencies: {}", currencies);

    Page<ProductResponseDto> resultPage = productService.getAllProducts(pageable, currencies);

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
  public ProductResponseDto getById(
          @PathVariable Long id,
          @RequestParam(defaultValue = "EUR", required = false) List<String> currencies
  ) {
    return productService.getProductById(id, currencies);
  }

  @PostMapping
  public ProductResponseDto create(@Valid @RequestBody ProductRequestDto request) {
    return productService.createProduct(request);
  }

  @PutMapping("/{id}")
  public ProductResponseDto update(@PathVariable Long id, @Valid @RequestBody ProductRequestDto request) {
    return productService.updateProduct(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    productService.softDeleteProduct(id);
  }
}