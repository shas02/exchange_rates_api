package com.exchange_rates.api.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Data
public class Constants {
    final static Map<String, String> DEFAULT_PROPERTY_MAP = new HashMap<>() {{
        put("fetchURLMonoBank", "https://api.monobank.ua/bank/currency");
        put("fetchURLPrivatBank", "https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=11");
        put("fetchURLMinfin", "https://api.minfin.com.ua/mb");
        put("minfinKey", "ab2639eb133c3fb48d07691ecaafd7c7c308ea72");
    }};

}
