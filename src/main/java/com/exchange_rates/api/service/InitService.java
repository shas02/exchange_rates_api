package com.exchange_rates.api.service;

import com.exchange_rates.api.data.domain.Property;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InitService {

    private final PropertyService propertyService;

    public InitService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostConstruct
    public void initProperties() {
        Constants.DEFAULT_PROPERTY_MAP.keySet().forEach(key -> addProperty(key, Constants.DEFAULT_PROPERTY_MAP.get(key)));
    }

    public void addProperty(String name, String value) {
        Property property = propertyService.getByName(name);
        if (property == null) propertyService.createProperty(name, value);
    }
}
