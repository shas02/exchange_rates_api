package com.exchange_rates.api.service.scheduler;

import com.exchange_rates.api.service.MinfinExchangeRatesUpdater;
import com.exchange_rates.api.service.MonoBankExchangeRatesUpdater;
import com.exchange_rates.api.service.PrivatBankExchangeRatesUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExchangeRatesScheduler {

    private final MinfinExchangeRatesUpdater minfinExchangeRatesUpdater;
    private final MonoBankExchangeRatesUpdater monoBankExchangeRatesUpdater;
    private final PrivatBankExchangeRatesUpdater privatBankExchangeRatesUpdater;

    public ExchangeRatesScheduler(MinfinExchangeRatesUpdater minfinExchangeRatesUpdater, MonoBankExchangeRatesUpdater monoBankExchangeRatesUpdater, PrivatBankExchangeRatesUpdater privatBankExchangeRatesUpdater) {
        this.minfinExchangeRatesUpdater = minfinExchangeRatesUpdater;
        this.monoBankExchangeRatesUpdater = monoBankExchangeRatesUpdater;
        this.privatBankExchangeRatesUpdater = privatBankExchangeRatesUpdater;
    }

    @Scheduled(fixedDelay = 86400000 /*24 hours*/, initialDelay = 1000)
    public void updateExchangeRates() {
        log.info("start update database");
        minfinExchangeRatesUpdater.updateExchangeRates();
        monoBankExchangeRatesUpdater.updateExchangeRates();
        privatBankExchangeRatesUpdater.updateExchangeRates();
        log.info("finish update database");
    }
}
