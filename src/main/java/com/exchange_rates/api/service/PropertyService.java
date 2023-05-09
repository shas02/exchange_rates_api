package com.exchange_rates.api.service;

import com.exchange_rates.api.data.domain.Property;
import com.exchange_rates.api.data.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private Map<String, Property> propertyMap = new HashMap<>();

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Scheduled(fixedDelay = 60000L)
    void updateMap() {
        Map<String, Property> newMap = new HashMap<>();
        getAll().forEach(p -> newMap.put(p.getName(), p));
        propertyMap = newMap;
    }

    public List<Property> getAll() {
        return propertyRepository.findAll();
    }

    public Property getById(Long id) {
        return propertyRepository.findById(id).orElse(null);
    }

    public Property getByName(String name) {
        return propertyRepository.findByName(name).orElse(null);
    }

    public String get(String name, String defaultValue) {
        Property entity = propertyMap.get(name);
        if (entity == null) entity = getByName(name);
        return (entity != null) ? entity.getValue() : defaultValue;
    }

    public Property save(Property property) {
        return propertyRepository.save(property);
    }

    public List<Property> saveAll(Iterable<Property> properties) {
        return propertyRepository.saveAll(properties);
    }

    public Property updateExchangeRate(Long id, Property property) {
        Property existingProperty = getById(id);
        if (existingProperty == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property does not exist!");
        existingProperty.setName(property.getName());
        existingProperty.setValue(property.getValue());
        return propertyRepository.save(property);
    }

    Property createProperty(String name, String value) {
        Property property = getByName(name);
        if (property == null) {
            Property newProperty = new Property();
            newProperty.setName(name);
            newProperty.setValue(value);
            propertyMap.put(name, newProperty);
            return propertyRepository.save(newProperty);
        } else {
            log.info("Property with name" + property.getName() + "already exist");
            return null;
        }
    }

    public void deleteById(Long id) {
        propertyRepository.deleteById(id);
    }
}
