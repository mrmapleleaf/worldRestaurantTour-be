package com.pj.worldRestaurantTourbe.service;

import com.pj.worldRestaurantTourbe.repository.CountryRepository;
import com.pj.worldRestaurantTourbe.repository.RestaurantRepository;
import com.pj.worldRestaurantTourbe.type.entity.Countries;
import com.pj.worldRestaurantTourbe.type.entity.Restaurants;
import com.pj.worldRestaurantTourbe.type.error.CountryNotFoundException;
import com.pj.worldRestaurantTourbe.type.form.CompletedRestaurantFrom;
import com.pj.worldRestaurantTourbe.type.response.RestaurantResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CountryRepository countryRepository;

    public RestaurantResponse register(CompletedRestaurantFrom form){

        // check if country does not exist
        Countries country = countryRepository.findById(form.getCountryId()).orElseThrow(
                () -> new CountryNotFoundException("指定された国が存在しません")
        );

        // prepare restaurant object to save
        Restaurants restautants = new Restaurants();
        restautants.setCountries(country);
        restautants.setName(form.getName());
        restautants.setThoughts(form.getThoughts());
        restautants.setUrl(form.getUrl());

        // save restaurant object
        restautants = restaurantRepository.save(restautants);

        // prepare response
        RestaurantResponse response = new RestaurantResponse();
        response.setRestaurant(restautants);

        return response;
    }
}
