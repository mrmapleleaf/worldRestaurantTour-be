package com.pj.worldRestaurantTourbe.controller;

import com.pj.worldRestaurantTourbe.entity.Countries;
import com.pj.worldRestaurantTourbe.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("/allCountries")
    public List<Countries> getAllCountries() {
         return countryService.getAllCountries();
    }

    @PutMapping("/chooseNextCountry")
    public Countries updateIsNext(Long id, boolean is_next) {
        return countryService.updateIsNext(id, is_next);
    }
}
