package com.pj.worldRestaurantTourbe.service;

import com.pj.worldRestaurantTourbe.repository.CountryRepository;
import com.pj.worldRestaurantTourbe.repository.RestaurantRepository;
import com.pj.worldRestaurantTourbe.type.RestaurantItem;
import com.pj.worldRestaurantTourbe.type.entity.Countries;
import com.pj.worldRestaurantTourbe.type.entity.Restaurants;
import com.pj.worldRestaurantTourbe.type.error.CountryNotFoundException;
import com.pj.worldRestaurantTourbe.type.error.RestaurantNotFoundException;
import com.pj.worldRestaurantTourbe.type.form.CompletedRestaurantFrom;
import com.pj.worldRestaurantTourbe.type.response.AllRestaurantsResponse;
import com.pj.worldRestaurantTourbe.type.response.RestaurantDetailResponse;
import com.pj.worldRestaurantTourbe.type.response.RestaurantResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CountryRepository countryRepository;

    public AllRestaurantsResponse getAllRestaurant() {

        List<Restaurants> restaurantsList = restaurantRepository.findAll();

        List<RestaurantItem> items = restaurantsList.stream()
                .map(restaurants -> {
                    RestaurantItem item = new RestaurantItem();
                    item.setId(restaurants.getId());
                    item.setName(restaurants.getName());
                    item.setThoughts(restaurants.getThoughts());
                    item.setUrl(restaurants.getUrl());
                    item.setCountries(restaurants.getCountries());
                    return item;
                }).toList();

        return new AllRestaurantsResponse(items);
    }

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


    public RestaurantDetailResponse detail(int countryId) {

        // fetch Restaurant detail
        Restaurants restaurant = restaurantRepository.findByCountriesId(countryId);
        if(restaurant == null) {
            throw new RestaurantNotFoundException("対象のレストランが見つかりません");
        }

        // prepare response
        RestaurantDetailResponse response = new RestaurantDetailResponse();
        response.setRestaurant(restaurant);

        return response;
    }

    public RestaurantResponse delete(int id) {

        // fetch delete target
        Restaurants restaurants = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("対象のレストランが見つかりません"));

        // delete target
        restaurantRepository.delete(restaurants);

        // prepare response
        RestaurantResponse response = new RestaurantResponse();
        response.setRestaurant(restaurants);

        return response;
    }
}
