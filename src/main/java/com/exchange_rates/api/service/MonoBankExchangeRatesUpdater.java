package com.exchange_rates.api.service;

import com.exchange_rates.api.data.domain.ExchangeRate;
import com.exchange_rates.api.data.domain.MonoBankExchangeRate;
import com.exchange_rates.api.dto.ConnectionInfo;
import com.exchange_rates.api.dto.MonoBankExchangeRateDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MonoBankExchangeRatesUpdater implements ExchangeRatesFetcher {

    private final MonoBankExchangeRateService monoBankExchangeRateService;
    private final Connector connector;
    private final PropertyService propertyService;

    public MonoBankExchangeRatesUpdater(MonoBankExchangeRateService monoBankExchangeRateService, Connector connector, PropertyService propertyService) {
        this.monoBankExchangeRateService = monoBankExchangeRateService;
        this.connector = connector;
        this.propertyService = propertyService;
    }

    public void updateExchangeRates() {
        List<MonoBankExchangeRate> monoBankExchangeRates = getExchangeRates().stream().map(er -> (MonoBankExchangeRate) er).toList();
        monoBankExchangeRateService.saveAll(monoBankExchangeRates);
    }

    public List<ExchangeRate> getExchangeRates() {
        try {
            String url = propertyService.get("fetchURLMonoBank", "https://api.monobank.ua/bank/currency");
            ConnectionInfo connection = connector.connect(new URL(url));
            List<MonoBankExchangeRateDto> monoBankExchangeRates = (connection.getResponseCode() == 200)
                    ? new ObjectMapper().readValue(connection.getContent(), new TypeReference<List<MonoBankExchangeRateDto>>() {})
                    : new ArrayList<>();

            if (monoBankExchangeRates.size() > 0) return monoBankExchangeRates.stream()
                    .map(er -> {
                        MonoBankExchangeRate exchangeRate = new MonoBankExchangeRate();
                        exchangeRate.setCurrencyCodeA(CurrencyHelper.getInstance(er.getCurrencyCodeA()).getCurrencyCode());
                        exchangeRate.setCurrencyCodeB(CurrencyHelper.getInstance(er.getCurrencyCodeB()).getCurrencyCode());
                        exchangeRate.setDate(new Date(er.getDate()));
                        exchangeRate.setRateSell(er.getRateSell());
                        exchangeRate.setRateBuy(er.getRateBuy());
                        exchangeRate.setRateCross(er.getRateCross());
                        return (ExchangeRate)exchangeRate;
                    })
                    .toList();
        } catch (MalformedURLException e) {
            log.error("Error during creating url: ", e);
        } catch (JsonProcessingException e) {
            log.error("Error during converting json: ", e);
        } catch (Exception e) {
            log.error("Error during fetching MonoBankExchangeRates: ", e);
        }

        return new ArrayList<>();
    }
}
