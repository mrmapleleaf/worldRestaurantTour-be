package com.pj.worldRestaurantTourbe.service;

import com.pj.worldRestaurantTourbe.type.entity.Countries;
import com.pj.worldRestaurantTourbe.repository.CountryRepository;
import com.pj.worldRestaurantTourbe.type.error.CountryNotFoundException;
import com.pj.worldRestaurantTourbe.type.response.AllCountriesIndexResponce;
import com.pj.worldRestaurantTourbe.type.response.CountryResponse;
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
    public CountryResponse setNextCountry(long id) {

        // check If next country already exists
        List<Countries> countries = countryRepository.findByNextIs(true);
        if (!countries.isEmpty()) {
            throw new RuntimeException("次に行く国がすでに存在しています");
        }

        // fetch target country
        Countries targetCountry = countryRepository.findById(id).orElseThrow(() -> new CountryNotFoundException("更新対象の国が存在しません"));
        targetCountry.setNext(true);
        targetCountry.setUpdated_at(LocalDateTime.now());

        // set next country
        countryRepository.save(targetCountry);

        // prepare response
        CountryResponse response = new CountryResponse();
        response.setCountry(targetCountry);

        return response;
    }

    @Transactional
    public CountryResponse unsetNextCountry(long id) {

        // fetch target country
        Countries targetCountry = countryRepository.findById(id).orElseThrow(() -> new CountryNotFoundException("更新対象の国が存在しません"));
        targetCountry.setNext(false);
        targetCountry.setUpdated_at(LocalDateTime.now());

        // unset next country
        countryRepository.save(targetCountry);

        // prepare response
        CountryResponse response = new CountryResponse();
        response.setCountry(targetCountry);

        return response;
    }

    @Transactional
    public CountryResponse setCompleted(long id) {

        // fetch target country
        Countries targetCountry = countryRepository.findById(id).orElseThrow(() -> new CountryNotFoundException("更新対象の国が存在しません"));
        targetCountry.setNext(false);
        targetCountry.setCompleted(true);
        targetCountry.setUpdated_at(LocalDateTime.now());

        // set country completed
        countryRepository.save(targetCountry);

        // prepare response
        CountryResponse response = new CountryResponse();
        response.setCountry(targetCountry);

        return response;
    }
}
