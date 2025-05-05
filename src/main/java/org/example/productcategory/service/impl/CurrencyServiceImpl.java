package org.example.productcategory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.productcategory.dto.CurrencyConversionResult;
import org.example.productcategory.dto.CurrencyResponse;
import org.example.productcategory.exception.CurrencyConversionException;
import org.example.productcategory.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

  private final ExchangeRateServiceImpl exchangeRateService;

  @Override
  public CurrencyConversionResult convertToEUR(String fromCurrency, double amount) {
    log.info("Converting {} {} to EUR", amount, fromCurrency);

    if (fromCurrency.equalsIgnoreCase("EUR")) {
      log.debug("Currency is already EUR, no conversion needed.");
      return new CurrencyConversionResult(true, amount, 1.0);
    }

    String upperCurrency = fromCurrency.toUpperCase();
    CurrencyResponse response = fetchAndValidateRates(upperCurrency);

    Double rate = response.getRates().get(upperCurrency);
    validateRate(rate, "conversion from " + upperCurrency + " to EUR");

    double convertedAmount = amount / rate;
    return new CurrencyConversionResult(true, convertedAmount, rate);
  }

  @Override
  public Map<String, CurrencyConversionResult> convertFromEUR(List<String> toCurrencies, double eurAmount) {
    log.info("Converting {} EUR to currencies: {}", eurAmount, toCurrencies);
    Map<String, CurrencyConversionResult> results = new HashMap<>();

    if (toCurrencies.contains("EUR")) {
      results.put("EUR", new CurrencyConversionResult(true, eurAmount, 1.0));
      log.debug("Added EUR to results with 1.0 rate.");
    }

    List<String> filteredCurrencies = toCurrencies.stream()
            .filter(c -> !c.equalsIgnoreCase("EUR"))
            .map(String::toUpperCase)
            .distinct()
            .toList();

    if (!filteredCurrencies.isEmpty()) {
      String symbols = String.join(",", filteredCurrencies);
      CurrencyResponse response = fetchAndValidateRates(symbols);

      for (String currency : filteredCurrencies) {
        Double rate = response.getRates().get(currency);
        validateRate(rate, "conversion from EUR to " + currency);

        double convertedAmount = eurAmount * rate;
        results.put(currency, new CurrencyConversionResult(true, convertedAmount, rate));
      }
    }

    return results;
  }

  private CurrencyResponse fetchAndValidateRates(String symbols) {
    CurrencyResponse response = exchangeRateService.getRates(symbols);
    if (response == null || response.getRates() == null || !response.isSuccess()) {
      throw new CurrencyConversionException("Failed to retrieve exchange rates for: " + symbols);
    }
    return response;
  }

  private void validateRate(Double rate, String context) {
    if (rate == null || rate <= 0.0) {
      throw new CurrencyConversionException("Invalid or missing rate for " + context);
    }
  }
}