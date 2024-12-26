package com.pj.worldRestaurantTourbe.controller;

import com.pj.worldRestaurantTourbe.service.RestaurantService;
import com.pj.worldRestaurantTourbe.type.form.CompletedRestaurantFrom;
import com.pj.worldRestaurantTourbe.type.response.AllRestaurantsResponse;
import com.pj.worldRestaurantTourbe.type.response.RestaurantDetailResponse;
import com.pj.worldRestaurantTourbe.type.response.RestaurantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping(value = "/allRestaurants")
    public AllRestaurantsResponse getAllRestaurants() {
        return restaurantService.getAllRestaurant();
    }

    @PostMapping(value = "/register")
    public RestaurantResponse register(@RequestBody CompletedRestaurantFrom form) {
        return restaurantService.register(form);
    }

    @GetMapping(value = "/detail/{id}")
    public RestaurantDetailResponse detail(@PathVariable(name = "id") int id) {
        return restaurantService.detail(id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public RestaurantResponse delete(@PathVariable(name = "id") int id) {
        return restaurantService.delete(id);
    }
}
