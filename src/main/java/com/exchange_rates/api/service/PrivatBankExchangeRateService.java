package com.exchange_rates.api.service;

import com.exchange_rates.api.data.domain.PrivatBankExchangeRate;
import com.exchange_rates.api.data.repository.PrivatBankExchangeRateRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class PrivatBankExchangeRateService {
    private final PrivatBankExchangeRateRepository privatBankExchangeRateRepository;

    public PrivatBankExchangeRateService(PrivatBankExchangeRateRepository exchangeRateRepository) {
        this.privatBankExchangeRateRepository = exchangeRateRepository;
    }

    public List<PrivatBankExchangeRate> getAll() {
        return privatBankExchangeRateRepository.findAll();
    }

    List<PrivatBankExchangeRate> getAllForPeriod(Date from, Date to) {
        return privatBankExchangeRateRepository.findAllByDateBetween(from, to);
    }
    public PrivatBankExchangeRate getById(Long id) {
        return privatBankExchangeRateRepository.findById(id).orElse(null);
    }

    public PrivatBankExchangeRate save(PrivatBankExchangeRate privatBankExchangeRate) {
        return privatBankExchangeRateRepository.save(privatBankExchangeRate);
    }

    public List<PrivatBankExchangeRate> saveAll(Iterable<PrivatBankExchangeRate> privatBankExchangeRates){
        return privatBankExchangeRateRepository.saveAll(privatBankExchangeRates);
    }

    public PrivatBankExchangeRate updateExchangeRate(Long id, PrivatBankExchangeRate privatBankExchangeRate) {
        PrivatBankExchangeRate existingExchangeRate = getById(id);
        if (existingExchangeRate == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ExchangeRate does not exist!");
        existingExchangeRate.setCurrencyCodeA(privatBankExchangeRate.getCurrencyCodeA());
        existingExchangeRate.setCurrencyCodeB(privatBankExchangeRate.getCurrencyCodeB());
        existingExchangeRate.setDate(privatBankExchangeRate.getDate());
        existingExchangeRate.setRateSell(privatBankExchangeRate.getRateSell());
        existingExchangeRate.setRateBuy(privatBankExchangeRate.getRateBuy());
        existingExchangeRate.setRateCross(privatBankExchangeRate.getRateCross());
        return privatBankExchangeRateRepository.save(privatBankExchangeRate);
    }

    public void deleteById(Long id) {
        privatBankExchangeRateRepository.deleteById(id);
    }
}
