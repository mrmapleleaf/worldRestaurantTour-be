package com.pj.worldRestaurantTourbe.service;

import com.pj.worldRestaurantTourbe.entity.Countries;
import com.pj.worldRestaurantTourbe.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;
    public List<Countries> getAllCountries() {
        return countryRepository.findAll();
    }

    @Transactional
    public Countries updateIsNext(Long id, boolean is_next) {

        Countries targetCountry = countryRepository.findById(id).orElseThrow(() -> new RuntimeException("更新対象の国が存在しません"));

        targetCountry.set_next(is_next);
        targetCountry.setUpdated_at(LocalDateTime.now());

        countryRepository.save(targetCountry);

        return targetCountry;
    }
}
