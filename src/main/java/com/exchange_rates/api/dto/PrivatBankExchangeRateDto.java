package com.exchange_rates.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrivatBankExchangeRateDto {
    private String ccy;
    private String base_ccy;
    private String buy;
    private String sale;
}
