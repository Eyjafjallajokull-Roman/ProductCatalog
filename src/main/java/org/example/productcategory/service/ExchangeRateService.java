package org.example.productcategory.service;

import org.example.productcategory.dto.CurrencyResponse;

public interface ExchangeRateService {

  CurrencyResponse getRates(String symbols);

}
