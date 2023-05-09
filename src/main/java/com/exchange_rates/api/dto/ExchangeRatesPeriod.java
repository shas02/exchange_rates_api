package com.exchange_rates.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExchangeRatesPeriod {
    private String currencyCodeA;
    private String currencyCodeB;
    private Date from;
    private Date to;
    private Double rateSellMinfin;
    private Double rateBuyMinfin;
    private Double rateSellMonobank;
    private Double rateBuyMonobank;
    private Double rateSellPrivatbank;
    private Double rateBuyPrivatbank;
    private Double rateSellAverage;
    private Double rateBuyAverage;
}
