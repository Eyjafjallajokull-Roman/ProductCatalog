package org.example.productcategory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ProductResponseDto {

  private Long id;
  private String name;
  private String description;
  private BigDecimal priceInEur;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private Map<String, BigDecimal> convertedPrices;
  private Long categoryId;

}
