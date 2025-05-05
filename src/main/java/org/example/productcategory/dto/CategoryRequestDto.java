package org.example.productcategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {

  @NotBlank
  @Size(min = 2, max = 100)
  private String name;

  private Long parentId;

}