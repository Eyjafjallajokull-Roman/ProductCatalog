package org.example.productcategory.service;

import org.example.productcategory.dto.CurrencyConversionResult;

import java.util.List;
import java.util.Map;

public interface CurrencyService {
  CurrencyConversionResult convertToEUR(String fromCurrency, double amount);

  Map<String, CurrencyConversionResult> convertFromEUR(List<String> currencies, double eurAmount);
}