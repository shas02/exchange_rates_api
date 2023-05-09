package com.exchange_rates.api.service;

import com.exchange_rates.api.data.domain.ExchangeRate;
import com.exchange_rates.api.data.domain.MinfinExchangeRate;
import com.exchange_rates.api.dto.ConnectionInfo;
import com.exchange_rates.api.dto.MinfinExchangeRateDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MinfinExchangeRatesUpdater implements ExchangeRatesFetcher {

    private final MinfinExchangeRateService minfinExchangeRateService;
    private final Connector connector;
    private final PropertyService propertyService;

    public MinfinExchangeRatesUpdater(MinfinExchangeRateService minfinExchangeRateService, Connector connector, PropertyService propertyService) {
        this.minfinExchangeRateService = minfinExchangeRateService;
        this.connector = connector;
        this.propertyService = propertyService;
    }

    public void updateExchangeRates() {
        List<MinfinExchangeRate> minfinExchangeRates = getExchangeRates().stream().map(er -> (MinfinExchangeRate) er).toList();
        minfinExchangeRateService.saveAll(minfinExchangeRates);
    }

    public List<ExchangeRate> getExchangeRates() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try {
            String url = propertyService.get("fetchURLMinfin", "https://api.minfin.com.ua/mb");
            String key = propertyService.get("minfinKey", "ab2639eb133c3fb48d07691ecaafd7c7c308ea72");
            LocalDate dateObj = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = dateObj.format(formatter);
            ConnectionInfo connection = connector.connect(new URL( url + "/" + key + "/" + date));
            List<MinfinExchangeRateDto> minfinExchangeRates = (connection.getResponseCode() == 200)
                    ? new ObjectMapper().readValue(connection.getContent(), new TypeReference<List<MinfinExchangeRateDto>>() {})
                    : new ArrayList<>();

            if (minfinExchangeRates.size() > 0)
                for (int i = 0; i < 2; i++) {
                    MinfinExchangeRate exchangeRate = new MinfinExchangeRate();
                    exchangeRate.setCurrencyCodeA(minfinExchangeRates.get(i).getCurrency().toUpperCase());
                    exchangeRate.setCurrencyCodeB("UAH");
                    exchangeRate.setDate(convertToDate(minfinExchangeRates.get(i).getDate()));
                    exchangeRate.setRateSell(Double.valueOf(minfinExchangeRates.get(i).getBid()));
                    exchangeRate.setRateBuy(Double.valueOf(minfinExchangeRates.get(i).getAsk()));
                    exchangeRate.setRateCross((Double.parseDouble(minfinExchangeRates.get(i).getAsk()) + Double.parseDouble(minfinExchangeRates.get(i).getBid())) / 2);
                    exchangeRates.add(exchangeRate);
                }
        } catch (MalformedURLException e) {
            log.error("Error during creating url: ", e);
        } catch (JsonProcessingException e) {
            log.error("Error during converting json: ", e);
        } catch (Exception e) {
            log.error("Error during fetching MinfinExchangeRates: ", e);
        }

        return exchangeRates;
    }

    private Date convertToDate(String dateText) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(dateText);
    }

}
