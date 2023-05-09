package com.exchange_rates.api.service;

import com.exchange_rates.api.data.domain.ExchangeRate;
import com.exchange_rates.api.data.domain.PrivatBankExchangeRate;
import com.exchange_rates.api.dto.ConnectionInfo;
import com.exchange_rates.api.dto.PrivatBankExchangeRateDto;
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
public class PrivatBankExchangeRatesUpdater implements ExchangeRatesFetcher {
    private final PrivatBankExchangeRateService privatBankExchangeRateService;
    private final Connector connector;
    private final PropertyService propertyService;

    public PrivatBankExchangeRatesUpdater(PrivatBankExchangeRateService privatBankExchangeRateService, Connector connector, PropertyService propertyService) {
        this.privatBankExchangeRateService = privatBankExchangeRateService;
        this.connector = connector;
        this.propertyService = propertyService;
    }

    public void updateExchangeRates() {
        List<PrivatBankExchangeRate> privatBankExchangeRates = getExchangeRates().stream().map(er -> (PrivatBankExchangeRate) er).toList();
        privatBankExchangeRateService.saveAll(privatBankExchangeRates);
    }

    public List<ExchangeRate> getExchangeRates() {
        try {
            String url = propertyService.get("fetchURLPrivatBank", "https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=11");
            ConnectionInfo connection = connector.connect(new URL(url));
            List<PrivatBankExchangeRateDto> privatBankExchangeRates = (connection.getResponseCode() == 200)
                    ? new ObjectMapper().readValue(connection.getContent(), new TypeReference<List<PrivatBankExchangeRateDto>>() {})
                    : new ArrayList<>();

            if (privatBankExchangeRates.size() > 0) return privatBankExchangeRates.stream()
                    .map(er -> {
                        PrivatBankExchangeRate exchangeRate = new PrivatBankExchangeRate();
                        exchangeRate.setCurrencyCodeA(er.getCcy());
                        exchangeRate.setCurrencyCodeB(er.getBase_ccy());
                        exchangeRate.setDate(new Date(System.currentTimeMillis()));
                        exchangeRate.setRateSell(Double.valueOf(er.getSale()));
                        exchangeRate.setRateBuy(Double.valueOf(er.getBuy()));
                        exchangeRate.setRateCross((Double.parseDouble(er.getBuy()) + Double.parseDouble(er.getSale())) / 2);
                        return (ExchangeRate) exchangeRate;
                    })
                    .toList();
        } catch (MalformedURLException e) {
            log.error("Error during creating url: ", e);
        } catch (JsonProcessingException e) {
            log.error("Error during converting json: ", e);
        } catch (Exception e) {
            log.error("Error during fetching PrivatBankExchangeRates: ", e);
        }

        return new ArrayList<>();
    }
}
