package org.example.productcategory.feign;

import org.example.productcategory.dto.CurrencyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "currencyClient", url = "${fixer.api.url}")
public interface CurrencyExchangeClient {

  @GetMapping("/latest")
  CurrencyResponse getRates(
          @RequestParam("access_key") String accessKey,
          @RequestParam("symbols") String symbols,
          @RequestParam("format") int format
  );
}