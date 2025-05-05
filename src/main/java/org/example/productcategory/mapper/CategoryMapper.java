package org.example.productcategory.mapper;

import org.example.productcategory.dto.CategoryRequestDto;
import org.example.productcategory.dto.CategoryResponseDto;
import org.example.productcategory.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(source = "parent.id", target = "parentId")
  CategoryResponseDto toDto(Category category);

  @Mapping(target = "parent", ignore = true)
  Category toEntity(CategoryRequestDto dto);
}