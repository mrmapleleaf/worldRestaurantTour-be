package com.pj.worldRestaurantTourbe.controller;

import com.pj.worldRestaurantTourbe.entity.Countries;
import com.pj.worldRestaurantTourbe.entity.NextCountry;
import com.pj.worldRestaurantTourbe.entity.VisitedCountry;
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

    @GetMapping("/nextCountry")
    public List<Countries> getNextCountry(@RequestParam boolean next) {
        return countryService.getNextCountry(next);
    }

    @PutMapping("/changeNextCountry")
    public Countries changeNextCountry(@RequestBody NextCountry nextCountry) {

        return countryService.updateIsNext(nextCountry.getId(), nextCountry.isNext());
    }

    @PutMapping("/makeChosenCountryCompleted")
    public Countries makeCountryCompleted(@RequestBody VisitedCountry visitedCountry) {

        return countryService.updateCompleted(visitedCountry.getId(), visitedCountry.isNext(), visitedCountry.isCompleted());
    }
}
