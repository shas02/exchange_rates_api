package com.exchange_rates.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MinfinExchangeRateDto {
    private String id;
    private String pointDate;
    private String date;
    private String ask;
    private String bid;
    private String trendAsk;
    private String trendBid;
    private String currency;
}
