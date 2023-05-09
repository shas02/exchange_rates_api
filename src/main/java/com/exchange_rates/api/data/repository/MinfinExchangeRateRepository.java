package com.exchange_rates.api.data.repository;

import com.exchange_rates.api.data.domain.MinfinExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MinfinExchangeRateRepository extends JpaRepository<MinfinExchangeRate, Long> {
    List<MinfinExchangeRate> findAllByDateBetween(Date from, Date to);
}

