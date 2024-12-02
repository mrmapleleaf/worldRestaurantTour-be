package com.pj.worldRestaurantTourbe.controller;

import com.pj.worldRestaurantTourbe.type.entity.Countries;
import com.pj.worldRestaurantTourbe.service.CountryService;
import com.pj.worldRestaurantTourbe.type.form.NextCountryForm;
import com.pj.worldRestaurantTourbe.type.form.VisitedCountryForm;
import com.pj.worldRestaurantTourbe.type.response.AllCountriesIndexResponce;
import com.pj.worldRestaurantTourbe.type.response.NextCountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("/allCountries")
    public AllCountriesIndexResponce getAllCountries() {
         return countryService.getAllCountries();
    }

    @GetMapping("/nextCountry")
    public NextCountryResponse getNextCountry(@RequestParam boolean next) {
        return countryService.getNextCountry(next);
    }

    @PutMapping("/decideNextCountry")
    public Countries decideNextCountry(@RequestBody NextCountryForm nextCountryForm) {

        return countryService.setNextCountry(nextCountryForm.getId());
    }

    @PutMapping("/resetNextCountry")
    public Countries resetNextCountry(@RequestBody NextCountryForm nextCountryForm) {

        return countryService.unsetNextCountry(nextCountryForm.getId());
    }

    @PutMapping("/setCompleted")
    public Countries setCompleted(@RequestBody VisitedCountryForm visitedCountry) {

        return countryService.setCompleted(visitedCountry.getId());
    }
}
