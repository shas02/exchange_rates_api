package com.exchange_rates.api.controller;

import com.exchange_rates.api.dto.ExchangeRates;
import com.exchange_rates.api.dto.ExchangeRatesPeriod;
import com.exchange_rates.api.service.ExchangeRatesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/exchangeRates")
public class ExchangeRatesController {
    private final ExchangeRatesService exchangeRatesService;

    public ExchangeRatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

    @GetMapping
    ResponseEntity<List<ExchangeRates>> getExchangeRates() {
        List<ExchangeRates> exchangeRatesPage = exchangeRatesService.getExchangeRates();
        return ResponseEntity.ok(exchangeRatesPage);
    }

    @GetMapping("/period")
    ResponseEntity<List<ExchangeRatesPeriod>> getExchangeRatesForPeriod(@RequestParam String from,
                                                                        @RequestParam String to) throws ParseException {
        List<ExchangeRatesPeriod> exchangeRatesPeriod = exchangeRatesService.getExchangeRatesForPeriod(from, to);
        return ResponseEntity.ok(exchangeRatesPeriod);
    }

}
