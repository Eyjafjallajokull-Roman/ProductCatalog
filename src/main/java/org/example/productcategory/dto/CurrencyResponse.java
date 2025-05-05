package org.example.productcategory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponse {
  private boolean success;
  private Map<String, Double> rates;
}