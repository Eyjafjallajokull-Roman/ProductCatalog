package org.example.productcategory;

import org.example.productcategory.dto.CurrencyConversionResult;
import org.example.productcategory.dto.CurrencyResponse;
import org.example.productcategory.exception.CurrencyConversionException;
import org.example.productcategory.service.impl.CurrencyServiceImpl;
import org.example.productcategory.service.impl.ExchangeRateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceImplTest {

  private ExchangeRateServiceImpl exchangeRateService;
  private CurrencyServiceImpl currencyService;

  @BeforeEach
  void setUp() {
    exchangeRateService = mock(ExchangeRateServiceImpl.class);
    currencyService = new CurrencyServiceImpl(exchangeRateService);
  }

  @Test
  void convertToEUR_whenCurrencyIsEUR_returnsSameAmount() {
    CurrencyConversionResult result = currencyService.convertToEUR("EUR", 150.0);

    assertTrue(result.isSuccess());
    assertEquals(150.0, result.getConvertedAmount());
    assertEquals(1.0, result.getRateUsed());
  }

  @Test
  void convertToEUR_whenValidRate_returnsConvertedAmount() {
    CurrencyResponse response = new CurrencyResponse(true, Map.of("USD", 1.25));
    when(exchangeRateService.getRates("USD")).thenReturn(response);

    CurrencyConversionResult result = currencyService.convertToEUR("usd", 125.0);

    assertTrue(result.isSuccess());
    assertEquals(100.0, result.getConvertedAmount(), 0.0001);
    assertEquals(1.25, result.getRateUsed(), 0.0001);
  }

  @Test
  void convertToEUR_whenRateIsMissing_throwsException() {
    CurrencyResponse response = new CurrencyResponse(true, Map.of());
    when(exchangeRateService.getRates("JPY")).thenReturn(response);

    assertThrows(CurrencyConversionException.class, () ->
            currencyService.convertToEUR("JPY", 100.0));
  }

  @Test
  void convertFromEUR_whenValidCurrencies_returnsAllConversions() {
    List<String> currencies = List.of("USD", "GBP", "EUR");
    Map<String, Double> rates = Map.of("USD", 1.1, "GBP", 0.9);
    CurrencyResponse response = new CurrencyResponse(true, rates);

    when(exchangeRateService.getRates("USD,GBP")).thenReturn(response);

    Map<String, CurrencyConversionResult> result =
            currencyService.convertFromEUR(currencies, 100.0);

    assertEquals(3, result.size());

    assertEquals(110.0, result.get("USD").getConvertedAmount(), 0.0001);
    assertEquals(1.1, result.get("USD").getRateUsed(), 0.0001);

    assertEquals(90.0, result.get("GBP").getConvertedAmount(), 0.0001);
    assertEquals(0.9, result.get("GBP").getRateUsed(), 0.0001);

    assertEquals(100.0, result.get("EUR").getConvertedAmount(), 0.0001);
    assertEquals(1.0, result.get("EUR").getRateUsed(), 0.0001);
  }

  @Test
  void convertFromEUR_whenResponseIsNull_throwsException() {
    when(exchangeRateService.getRates("AUD")).thenReturn(null);

    assertThrows(CurrencyConversionException.class, () ->
            currencyService.convertFromEUR(List.of("AUD"), 100.0));
  }

  @Test
  void convertFromEUR_whenRateIsZero_throwsException() {
    CurrencyResponse response = new CurrencyResponse(true, Map.of("CHF", 0.0));
    when(exchangeRateService.getRates("CHF")).thenReturn(response);

    assertThrows(CurrencyConversionException.class, () ->
            currencyService.convertFromEUR(List.of("CHF"), 100.0));
  }
}