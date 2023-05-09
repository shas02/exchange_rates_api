package com.exchange_rates.api.service;

import com.exchange_rates.api.data.domain.MonoBankExchangeRate;
import com.exchange_rates.api.data.repository.MonoBankExchangeRateRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Date;
import java.util.List;

@Service
public class MonoBankExchangeRateService {
    private final MonoBankExchangeRateRepository monoBankExchangeRateRepository;

    public MonoBankExchangeRateService(MonoBankExchangeRateRepository exchangeRateRepository) {
        this.monoBankExchangeRateRepository = exchangeRateRepository;
    }

    public List<MonoBankExchangeRate> getAll() {
        return monoBankExchangeRateRepository.findAll();
    }

    List<MonoBankExchangeRate> getAllForPeriod(Date from, Date to) {
        return monoBankExchangeRateRepository.findAllByDateBetween(from, to);
    }

    public MonoBankExchangeRate getById(Long id) {
        return monoBankExchangeRateRepository.findById(id).orElse(null);
    }

    public MonoBankExchangeRate save(MonoBankExchangeRate monoBankExchangeRate) {
        return monoBankExchangeRateRepository.save(monoBankExchangeRate);
    }

    public List<MonoBankExchangeRate> saveAll(Iterable<MonoBankExchangeRate> monoBankExchangeRates){
        return monoBankExchangeRateRepository.saveAll(monoBankExchangeRates);
    }

    public MonoBankExchangeRate updateExchangeRate(Long id, MonoBankExchangeRate monoBankExchangeRate) {
        MonoBankExchangeRate existingExchangeRate = getById(id);
        if (existingExchangeRate == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ExchangeRate does not exist!");
        existingExchangeRate.setCurrencyCodeA(monoBankExchangeRate.getCurrencyCodeA());
        existingExchangeRate.setCurrencyCodeB(monoBankExchangeRate.getCurrencyCodeB());
        existingExchangeRate.setDate(monoBankExchangeRate.getDate());
        existingExchangeRate.setRateSell(monoBankExchangeRate.getRateSell());
        existingExchangeRate.setRateBuy(monoBankExchangeRate.getRateBuy());
        existingExchangeRate.setRateCross(monoBankExchangeRate.getRateCross());
        return monoBankExchangeRateRepository.save(monoBankExchangeRate);
    }

    public void deleteById(Long id) {
        monoBankExchangeRateRepository.deleteById(id);
    }
}
