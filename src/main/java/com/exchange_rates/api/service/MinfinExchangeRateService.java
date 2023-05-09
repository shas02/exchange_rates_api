package com.exchange_rates.api.service;

import com.exchange_rates.api.data.domain.MinfinExchangeRate;
import com.exchange_rates.api.data.repository.MinfinExchangeRateRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Date;
import java.util.List;

@Service
public class MinfinExchangeRateService {
    private final MinfinExchangeRateRepository minfinExchangeRateRepository;

    public MinfinExchangeRateService(MinfinExchangeRateRepository exchangeRateRepository) {
        this.minfinExchangeRateRepository = exchangeRateRepository;
    }

    public List<MinfinExchangeRate> getAll() {
        return minfinExchangeRateRepository.findAll();
    }

    List<MinfinExchangeRate> getAllForPeriod(Date from, Date to) {
        return minfinExchangeRateRepository.findAllByDateBetween(from, to);
    }

    public MinfinExchangeRate getById(Long id) {
        return minfinExchangeRateRepository.findById(id).orElse(null);
    }

    public MinfinExchangeRate save(MinfinExchangeRate minfinExchangeRate) {
        return minfinExchangeRateRepository.save(minfinExchangeRate);
    }

    public List<MinfinExchangeRate> saveAll(Iterable<MinfinExchangeRate> minfinExchangeRates){
        return minfinExchangeRateRepository.saveAll(minfinExchangeRates);
    }

    public MinfinExchangeRate updateExchangeRate(Long id, MinfinExchangeRate minfinExchangeRate) {
        MinfinExchangeRate existingExchangeRate = getById(id);
        if (existingExchangeRate == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ExchangeRate does not exist!");
        existingExchangeRate.setCurrencyCodeA(minfinExchangeRate.getCurrencyCodeA());
        existingExchangeRate.setCurrencyCodeB(minfinExchangeRate.getCurrencyCodeB());
        existingExchangeRate.setDate(minfinExchangeRate.getDate());
        existingExchangeRate.setRateSell(minfinExchangeRate.getRateSell());
        existingExchangeRate.setRateBuy(minfinExchangeRate.getRateBuy());
        existingExchangeRate.setRateCross(minfinExchangeRate.getRateCross());
        return minfinExchangeRateRepository.save(minfinExchangeRate);
    }

    public void deleteById(Long id) {
        minfinExchangeRateRepository.deleteById(id);
    }
}
