package org.example.productcategory.service.impl;

import org.example.productcategory.dto.CurrencyResponse;
import org.example.productcategory.feign.CurrencyExchangeClient;
import org.example.productcategory.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

  private final CurrencyExchangeClient currencyClient;
  private final String apiKey;

  public ExchangeRateServiceImpl(CurrencyExchangeClient currencyClient,
                                 @Value("${fixer.api.key}") String apiKey) {
    this.currencyClient = currencyClient;
    this.apiKey = apiKey;
  }

  @Cacheable(value = "fixerRates", key = "#symbols")
  public CurrencyResponse getRates(String symbols) {
    return currencyClient.getRates(apiKey, symbols, 1);
  }

}