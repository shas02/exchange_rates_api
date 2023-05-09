package com.exchange_rates.api.data.repository;

import com.exchange_rates.api.data.domain.PrivatBankExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PrivatBankExchangeRateRepository extends JpaRepository<PrivatBankExchangeRate, Long> {
    List<PrivatBankExchangeRate> findAllByDateBetween(Date from, Date to);

}
