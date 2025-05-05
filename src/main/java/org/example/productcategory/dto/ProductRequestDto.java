package org.example.productcategory.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {

  @NotBlank
  private String name;

  private String description;

  @NotNull
  @DecimalMin("0.0")
  private BigDecimal price;

  @NotBlank
  @Size(min = 3, max = 3)
  private String currency;

  @NotNull
  private Long categoryId;

}