package org.example.productcategory.dto;

import lombok.Data;

@Data
public class CategoryResponseDto {

  private Long id;
  private String name;
  private Long parentId;

}