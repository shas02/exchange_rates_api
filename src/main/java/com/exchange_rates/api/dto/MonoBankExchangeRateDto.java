package com.exchange_rates.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonoBankExchangeRateDto {
    private int currencyCodeA;
    private int currencyCodeB;
    private long date;
    private double rateSell;
    private double rateBuy;
    private double rateCross;
}
