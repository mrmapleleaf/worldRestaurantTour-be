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

    public List<Countries> getNextCountry(boolean next) {
        return countryRepository.findByNextIs(next);
    }

    @Transactional
    public Countries setNextCountry(long id) {

        List<Countries> countries = countryRepository.findByNextIs(true);
        if (!countries.isEmpty()) {
            throw new RuntimeException("次に行く国がすでに存在しています");
        }

        Countries targetCountry = countryRepository.findById(id).orElseThrow(() -> new RuntimeException("更新対象の国が存在しません"));

        targetCountry.setNext(true);
        targetCountry.setUpdated_at(LocalDateTime.now());

        countryRepository.save(targetCountry);

        return targetCountry;
    }

    @Transactional
    public Countries unsetNextCountry(long id) {

        Countries targetCountry = countryRepository.findById(id).orElseThrow(() -> new RuntimeException("更新対象の国が存在しません"));

        targetCountry.setNext(false);
        targetCountry.setUpdated_at(LocalDateTime.now());
        countryRepository.save(targetCountry);

        return targetCountry;
    }

    @Transactional
    public Countries updateCompleted(long id, boolean next, boolean completed) {

        Countries targetCountry = countryRepository.findById(id).orElseThrow(() -> new RuntimeException("更新対象の国が存在しません"));

        targetCountry.setNext(next);
        targetCountry.setCompleted(completed);
        targetCountry.setUpdated_at(LocalDateTime.now());

        countryRepository.save(targetCountry);

        return targetCountry;
    }
}
