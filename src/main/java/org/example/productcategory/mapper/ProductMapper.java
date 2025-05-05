package org.example.productcategory.mapper;

import org.example.productcategory.dto.ProductRequestDto;
import org.example.productcategory.dto.ProductResponseDto;
import org.example.productcategory.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mapping(source = "price", target = "priceInEur")
  @Mapping(source = "category.id", target = "categoryId")
  ProductResponseDto toDto(Product product);

  @Mapping(source = "categoryId", target = "category.id")
  Product toEntity(ProductRequestDto dto);
}