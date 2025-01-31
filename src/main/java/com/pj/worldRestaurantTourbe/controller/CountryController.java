package com.pj.worldRestaurantTourbe.controller;

import com.pj.worldRestaurantTourbe.service.CountryService;
import com.pj.worldRestaurantTourbe.type.form.NextCountryForm;
import com.pj.worldRestaurantTourbe.type.form.VisitedCountryForm;
import com.pj.worldRestaurantTourbe.type.response.AllCountriesIndexResponce;
import com.pj.worldRestaurantTourbe.type.response.CountryResponse;
import com.pj.worldRestaurantTourbe.type.response.NextCountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public NextCountryResponse getNextCountry() {
        return countryService.getNextCountry();
    }

    @PutMapping("/decideNextCountry")
    public CountryResponse decideNextCountry(@RequestBody NextCountryForm nextCountryForm) {

        return countryService.setNextCountry(nextCountryForm.getId());
    }

    @PutMapping("/resetNextCountry")
    public CountryResponse resetNextCountry(@RequestBody NextCountryForm nextCountryForm) {

        return countryService.unsetNextCountry(nextCountryForm.getId());
    }

    @PutMapping("/setCompleted")
    public CountryResponse setCompleted(@RequestBody VisitedCountryForm visitedCountry) {

        return countryService.setCompleted(visitedCountry.getId());
    }
}
