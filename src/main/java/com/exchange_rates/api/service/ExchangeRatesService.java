package com.exchange_rates.api.service;

import com.exchange_rates.api.data.domain.ExchangeRate;
import com.exchange_rates.api.data.domain.MinfinExchangeRate;
import com.exchange_rates.api.data.domain.MonoBankExchangeRate;
import com.exchange_rates.api.data.domain.PrivatBankExchangeRate;
import com.exchange_rates.api.dto.ExchangeRates;
import com.exchange_rates.api.dto.ExchangeRatesPeriod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExchangeRatesService {

    private final MinfinExchangeRateService minfinExchangeRateService;
    private final MonoBankExchangeRateService monoBankExchangeRateService;
    private final PrivatBankExchangeRateService privatBankExchangeRateService;

    public ExchangeRatesService(MinfinExchangeRateService minfinExchangeRateService,
                                MonoBankExchangeRateService monoBankExchangeRateService,
                                PrivatBankExchangeRateService privatBankExchangeRateService) {
        this.minfinExchangeRateService = minfinExchangeRateService;
        this.monoBankExchangeRateService = monoBankExchangeRateService;
        this.privatBankExchangeRateService = privatBankExchangeRateService;
    }

    public List<ExchangeRates> getExchangeRates() {
        Date startOfDay = getStartOfDayDate();
        Date endOfDay = getEndOfDayDate();

        List<MonoBankExchangeRate> monoBankExchangeRates = monoBankExchangeRateService.getAllForPeriod(startOfDay, endOfDay);
        List<PrivatBankExchangeRate> privatBankExchangeRates = privatBankExchangeRateService.getAllForPeriod(startOfDay, endOfDay);
        List<MinfinExchangeRate> minfinExchangeRates = minfinExchangeRateService.getAllForPeriod(startOfDay, endOfDay);

        List<ExchangeRates> exchangeRates = new ArrayList<>();

        privatBankExchangeRates.forEach(per -> {
            MonoBankExchangeRate monoBankExchangeRate = monoBankExchangeRates.stream()
                    .filter(mer -> mer.getCurrencyCodeA().equals(per.getCurrencyCodeA()))
                    .findFirst()
                    .orElse(null);

            MinfinExchangeRate minfinExchangeRate = minfinExchangeRates.stream()
                    .filter(mfer -> mfer.getCurrencyCodeA().equals(per.getCurrencyCodeA()))
                    .findFirst()
                    .orElse(null);

            exchangeRates.add(new ExchangeRates(
                    per.getCurrencyCodeA(),
                    per.getCurrencyCodeB(),
                    per.getDate(),
                    (minfinExchangeRate != null) ? minfinExchangeRate.getRateSell() : null,
                    (minfinExchangeRate != null) ? minfinExchangeRate.getRateBuy() : null,
                    (monoBankExchangeRate != null) ? monoBankExchangeRate.getRateSell() : null,
                    (monoBankExchangeRate != null) ? monoBankExchangeRate.getRateBuy() : null,
                    per.getRateSell(),
                    per.getRateBuy(),
                    (minfinExchangeRate != null && monoBankExchangeRate != null) ?
                            (monoBankExchangeRate.getRateSell() + minfinExchangeRate.getRateSell() + per.getRateSell()) / 3
                            : per.getRateSell(),
                    (minfinExchangeRate != null && monoBankExchangeRate != null) ?
                            (monoBankExchangeRate.getRateBuy() + minfinExchangeRate.getRateBuy() + per.getRateBuy()) / 3
                            : per.getRateBuy()
            ));
        });

        return exchangeRates;
    }

    public List<ExchangeRatesPeriod> getExchangeRatesForPeriod(String from, String to) throws ParseException {
        Date fromDate = convertToDate(from);
        Date toDate = convertToDate(to);

        List<MonoBankExchangeRate> monoBankExchangeRates = monoBankExchangeRateService.getAllForPeriod(fromDate, toDate);
        List<PrivatBankExchangeRate> privatBankExchangeRates = privatBankExchangeRateService.getAllForPeriod(fromDate, toDate);
        List<MinfinExchangeRate> minfinExchangeRates = minfinExchangeRateService.getAllForPeriod(fromDate, toDate);

        Set<String> uniqCurrencyCodes = privatBankExchangeRates.stream().map(ExchangeRate::getCurrencyCodeA).collect(Collectors.toSet());

        List<ExchangeRatesPeriod> exchangeRatesPeriods = new ArrayList<>();

        uniqCurrencyCodes.forEach(cc -> {
            List<MinfinExchangeRate> currencyMinfinExchangeRates = minfinExchangeRates.stream()
                    .filter(mfer -> mfer.getCurrencyCodeA().equals(cc))
                    .toList();
            List<MonoBankExchangeRate> currencyMonoBankExchangeRates = monoBankExchangeRates.stream()
                    .filter(mer -> mer.getCurrencyCodeA().equals(cc) && mer.getCurrencyCodeA().equals("UAH"))
                    .toList();
            List<PrivatBankExchangeRate> currencyPrivatBankExchangeRates = privatBankExchangeRates.stream()
                    .filter(per -> per.getCurrencyCodeA().equals(cc))
                    .toList();

            double averageRateSellMinfin = (currencyMinfinExchangeRates.size() > 0) ?
                    currencyMinfinExchangeRates.stream().map(ExchangeRate::getRateSell)
                            .reduce(0.0, Double::sum) / currencyMinfinExchangeRates.size()
                    : 0;
            double averageRateBuyMinfin = (currencyMinfinExchangeRates.size() > 0) ?
                    currencyMinfinExchangeRates.stream().map(ExchangeRate::getRateBuy)
                            .reduce(0.0, Double::sum) / currencyMinfinExchangeRates.size()
                    : 0;
            double averageRateSellMonobank = (currencyMonoBankExchangeRates.size() > 0) ?
                    currencyMonoBankExchangeRates.stream().map(ExchangeRate::getRateSell)
                            .reduce(0.0, Double::sum) / currencyMonoBankExchangeRates.size()
                    : 0;
            double averageRateBuyMonobank = (currencyMonoBankExchangeRates.size() > 0) ?
                    currencyMonoBankExchangeRates.stream().map(ExchangeRate::getRateBuy)
                            .reduce(0.0, Double::sum) / currencyMonoBankExchangeRates.size()
                    : 0;
            double averageRateSellPrivatbank = (currencyPrivatBankExchangeRates.size() > 0) ?
                    currencyPrivatBankExchangeRates.stream().map(ExchangeRate::getRateSell)
                            .reduce(0.0, Double::sum) / currencyPrivatBankExchangeRates.size()
                    : 0;
            double averageRateBuyPrivatbank = (currencyPrivatBankExchangeRates.size() > 0) ?
                    currencyPrivatBankExchangeRates.stream().map(ExchangeRate::getRateBuy)
                            .reduce(0.0, Double::sum) / currencyPrivatBankExchangeRates.size()
                    : 0;

            double averageRateSell = (averageRateSellMinfin + averageRateSellMonobank + averageRateSellPrivatbank) / 3;
            double averageRateBuy = (averageRateBuyMinfin + averageRateBuyMonobank + averageRateBuyPrivatbank) / 3;

            exchangeRatesPeriods.add(new ExchangeRatesPeriod(
                    cc,
                    "UAH",
                    fromDate,
                    toDate,
                    averageRateSellMinfin,
                    averageRateBuyMinfin,
                    averageRateSellMonobank,
                    averageRateBuyMonobank,
                    averageRateSellPrivatbank,
                    averageRateBuyPrivatbank,
                    averageRateSell,
                    averageRateBuy
            ));
        });

        return exchangeRatesPeriods;
    }

    private Date convertToDate(String dateText) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateText);
    }

    private Date getStartOfDayDate() {
        Calendar calStartOfDay = Calendar.getInstance();
        calStartOfDay.set(Calendar.HOUR_OF_DAY, 0);
        calStartOfDay.set(Calendar.MINUTE, 0);
        calStartOfDay.set(Calendar.SECOND, 0);
        calStartOfDay.set(Calendar.MILLISECOND, 0);
        return calStartOfDay.getTime();
    }

    private Date getEndOfDayDate() {
        Calendar calEndOfDay = Calendar.getInstance();
        calEndOfDay.set(Calendar.HOUR_OF_DAY, 23);
        calEndOfDay.set(Calendar.MINUTE, 59);
        calEndOfDay.set(Calendar.SECOND, 59);
        calEndOfDay.set(Calendar.MILLISECOND, 99);
        return calEndOfDay.getTime();
    }
}
