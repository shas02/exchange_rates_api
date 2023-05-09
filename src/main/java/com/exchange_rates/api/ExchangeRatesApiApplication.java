package com.exchange_rates.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExchangeRatesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeRatesApiApplication.class, args);
    }

}
