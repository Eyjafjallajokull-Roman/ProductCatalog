package org.example.productcategory.dto;

import lombok.Data;

@Data
public class CategoryParentTreeDto {
  private Long id;
  private String name;
  private CategoryParentTreeDto parent;
}