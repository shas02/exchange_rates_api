package com.exchange_rates.api.data.repository;

import com.exchange_rates.api.data.domain.MonoBankExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface MonoBankExchangeRateRepository extends JpaRepository<MonoBankExchangeRate, Long> {

    List<MonoBankExchangeRate> findAllByDateBetween(Date from, Date to);
}
