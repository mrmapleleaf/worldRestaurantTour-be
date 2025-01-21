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
import java.util.Optional;

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
                    return new RestaurantItem(
                            restaurants.getId(),
                            restaurants.getName(),
                            restaurants.getThoughts(),
                            restaurants.getUrl(),
                            restaurants.getCountries()
                    );
                }).toList();

        return new AllRestaurantsResponse(items);
    }

    public RestaurantResponse register(CompletedRestaurantFrom form){

        // check if country does not exist
        Countries country = countryRepository.findById(form.getCountryId()).orElseThrow(
                () -> new CountryNotFoundException("指定された国が存在しません")
        );

        // prepare restaurant object to save
        Restaurants restaurants = new Restaurants();
        restaurants.setCountries(country);
        restaurants.setName(form.getName());
        restaurants.setThoughts(form.getThoughts());
        restaurants.setUrl(form.getUrl());

        // save restaurant object
        restaurants = restaurantRepository.save(restaurants);

        // return response
        return new RestaurantResponse(
                restaurants.getId(),
                restaurants.getName(),
                restaurants.getThoughts(),
                restaurants.getUrl(),
                restaurants.getCountries()
        );
    }


    public RestaurantDetailResponse detail(int id) {

        // fetch Restaurant detail
        Optional<Restaurants> restaurant = restaurantRepository.findById(id);
        if(restaurant.isEmpty()) {
            throw new RestaurantNotFoundException("対象のレストランが見つかりません");
        }

        // return response
        return new RestaurantDetailResponse(
                restaurant.get().getId(),
                restaurant.get().getName(),
                restaurant.get().getThoughts(),
                restaurant.get().getUrl(),
                restaurant.get().getCountries()
        );
    }

    public RestaurantResponse delete(int id) {

        // fetch delete target
        Restaurants restaurants = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("対象のレストランが見つかりません"));

        // delete target
        restaurantRepository.delete(restaurants);

        // return response
        return new RestaurantResponse(
                restaurants.getId(),
                restaurants.getName(),
                restaurants.getThoughts(),
                restaurants.getUrl(),
                restaurants.getCountries()
        );
    }
}
