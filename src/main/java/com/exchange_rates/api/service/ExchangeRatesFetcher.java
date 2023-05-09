package com.exchange_rates.api.service;

import com.exchange_rates.api.data.domain.ExchangeRate;
import java.util.List;

public interface ExchangeRatesFetcher {
    List<ExchangeRate> getExchangeRates();
}
