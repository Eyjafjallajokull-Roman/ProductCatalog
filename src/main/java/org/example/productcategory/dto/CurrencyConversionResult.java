package org.example.productcategory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyConversionResult {
  private boolean success;
  private double convertedAmount;
  private double rateUsed;
}