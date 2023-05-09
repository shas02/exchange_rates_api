package com.exchange_rates.api.data.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class MonoBankExchangeRate extends ExchangeRate {

}
