package org.example.productcategory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.productcategory.dto.CurrencyConversionResult;
import org.example.productcategory.dto.ProductRequestDto;
import org.example.productcategory.dto.ProductResponseDto;
import org.example.productcategory.entity.Product;
import org.example.productcategory.exception.BadRequestException;
import org.example.productcategory.exception.ProductNotFoundException;
import org.example.productcategory.mapper.ProductMapper;
import org.example.productcategory.repository.ProductRepository;
import org.example.productcategory.service.CategoryService;
import org.example.productcategory.service.CurrencyService;
import org.example.productcategory.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryService categoryService;
  private final ProductMapper productMapper;
  private final CurrencyService currencyService;

  @Override
  public Page<ProductResponseDto> getAllProducts(Pageable pageable, List<String> currencies) {
    log.info("Fetching all products with currencies: {}", currencies);
    Map<String, BigDecimal> conversionRates = getConversionRates(currencies);

    return productRepository.findAllNotDeleted(pageable)
            .map(product -> {
              ProductResponseDto dto = productMapper.toDto(product);
              dto.setConvertedPrices(convertPrices(product.getPrice(), conversionRates));
              return dto;
            });
  }

  @Override
  public ProductResponseDto getProductById(Long id, List<String> currencies) {
    log.info("Fetching product by ID: {} with currencies: {}", id, currencies);
    Product product = productRepository.findByIdNotDeleted(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

    ProductResponseDto dto = productMapper.toDto(product);
    dto.setConvertedPrices(convertPrices(product.getPrice(), getConversionRates(currencies)));

    return dto;
  }

  @Override
  @Transactional
  public ProductResponseDto createProduct(ProductRequestDto request) {
    log.info("Creating product with request: {}", request);
    CurrencyConversionResult conversion = currencyService.convertToEUR(
            request.getCurrency(),
            request.getPrice().doubleValue()
    );

    if (!conversion.isSuccess()) {
      throw new BadRequestException("Unsupported or unconvertible currency: " + request.getCurrency());
    }

    Product product = productMapper.toEntity(request);
    product.setPrice(BigDecimal.valueOf(conversion.getConvertedAmount()));
    product.setCurrency("EUR");
    product.setCategory(categoryService.getCategoryEntityById(request.getCategoryId()));

    Product saved = productRepository.save(product);
    log.info("Product created with ID: {}", saved.getId());
    return productMapper.toDto(saved);
  }

  @Override
  @Transactional
  public ProductResponseDto updateProduct(Long id, ProductRequestDto request) {
    log.info("Updating product ID {} with request: {}", id, request);
    Product existing = productRepository.findByIdNotDeleted(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

    CurrencyConversionResult conversion = currencyService.convertToEUR(
            request.getCurrency(),
            request.getPrice().doubleValue()
    );

    if (!conversion.isSuccess()) {
      throw new BadRequestException("Unsupported or unconvertible currency: " + request.getCurrency());
    }

    existing.setName(request.getName());
    existing.setDescription(request.getDescription());
    existing.setPrice(BigDecimal.valueOf(conversion.getConvertedAmount()));
    existing.setCurrency("EUR");

    existing.setCategory(categoryService.getCategoryEntityById(request.getCategoryId()));

    Product updated = productRepository.save(existing);
    log.info("Product updated with ID: {}", updated.getId());
    return productMapper.toDto(updated);
  }

  public void softDeleteProduct(Long id) {
    log.info("Soft deleting product ID: {}", id);
    Product product = productRepository.findByIdNotDeleted(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

    product.setDeleted(true);
    productRepository.save(product);
    log.info("Product ID {} marked as deleted", id);
  }

  private boolean onlyEur(List<String> currencies) {
    return currencies.size() == 1 && currencies.get(0).equalsIgnoreCase("EUR");
  }

  private Map<String, BigDecimal> getConversionRates(List<String> currencies) {
    log.debug("Fetching conversion rates for currencies: {}", currencies);
    if (currencies == null || currencies.isEmpty() || onlyEur(currencies)) {
      return Map.of("EUR", BigDecimal.ONE);
    }

    Map<String, CurrencyConversionResult> rateMap = currencyService.convertFromEUR(currencies, 1.0);

    return currencies.stream()
            .map(String::toUpperCase)
            .filter(rateMap::containsKey)
            .filter(cur -> rateMap.get(cur).isSuccess())
            .collect(Collectors.toMap(
                    cur -> cur,
                    cur -> BigDecimal.valueOf(rateMap.get(cur).getRateUsed())
            ));
  }

  private Map<String, BigDecimal> convertPrices(BigDecimal eurPrice, Map<String, BigDecimal> conversionRates) {
    log.debug("Converting price {} EUR using rates: {}", eurPrice, conversionRates);
    return conversionRates.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> eurPrice.multiply(entry.getValue())
            ));
  }
}