package org.example.productcategory.mapper;

import org.example.productcategory.dto.CategoryParentTreeDto;
import org.example.productcategory.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryParentTreeMapper {

  @Mapping(target = "parent", ignore = true)
  CategoryParentTreeDto toBaseDto(Category category);

  default CategoryParentTreeDto toDtoRecursive(Category category) {
    if (category == null) return null;

    CategoryParentTreeDto dto = toBaseDto(category);
    dto.setParent(toDtoRecursive(category.getParent()));
    return dto;
  }
}