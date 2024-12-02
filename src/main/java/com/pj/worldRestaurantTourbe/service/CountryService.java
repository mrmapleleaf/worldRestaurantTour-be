package com.pj.worldRestaurantTourbe.service;

import com.pj.worldRestaurantTourbe.type.entity.Countries;
import com.pj.worldRestaurantTourbe.repository.CountryRepository;
import com.pj.worldRestaurantTourbe.type.response.AllCountriesIndexResponce;
import com.pj.worldRestaurantTourbe.type.response.NextCountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    public AllCountriesIndexResponce getAllCountries() {

        // fetch all countries
        List<Countries> AllCountries = countryRepository.findAll();

        // prepare responce
        AllCountriesIndexResponce responce = new AllCountriesIndexResponce();
        responce.setCountries(AllCountries);

        return responce;
    }

    public NextCountryResponse getNextCountry(boolean next) {

        // fetch next country
        List<Countries> nextCountry = countryRepository.findByNextIs(next);

        // prepare response
        NextCountryResponse response = new NextCountryResponse();
        response.setNextCountry(nextCountry);
         
        return response;
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
    public Countries setCompleted(long id) {

        Countries targetCountry = countryRepository.findById(id).orElseThrow(() -> new RuntimeException("更新対象の国が存在しません"));

        targetCountry.setNext(false);
        targetCountry.setCompleted(true);
        targetCountry.setUpdated_at(LocalDateTime.now());

        countryRepository.save(targetCountry);

        return targetCountry;
    }
}
