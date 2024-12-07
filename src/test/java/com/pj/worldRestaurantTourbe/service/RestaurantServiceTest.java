package com.pj.worldRestaurantTourbe.service;

import com.pj.worldRestaurantTourbe.repository.CountryRepository;
import com.pj.worldRestaurantTourbe.repository.RestaurantRepository;
import com.pj.worldRestaurantTourbe.type.entity.Countries;
import com.pj.worldRestaurantTourbe.type.entity.Restaurants;
import com.pj.worldRestaurantTourbe.type.form.CompletedRestaurantFrom;
import com.pj.worldRestaurantTourbe.type.response.RestaurantResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RestaurantService.class)
public class RestaurantServiceTest {

    @Autowired
    RestaurantService restaurantService;

    @MockBean
    RestaurantRepository restaurantRepositoryMock;

    @MockBean
    CountryRepository countryRepositoryMock;

    @Nested
    @DisplayName("レストラン登録")
    class RegisterRestaurant {

        @Test
        @DisplayName("正常系")
        public void register() {

            // prepare test data
            CompletedRestaurantFrom form = new CompletedRestaurantFrom();
            form.setName("レストラン");
            form.setThoughts("good");
            form.setUrl("http://sample.com");
            form.setCountryId(1);

            Countries country = new Countries();
            country.setId(1);

            Optional<Countries> countryOptional = Optional.of(country);

            Restaurants restaurant = new Restaurants();
            restaurant.setId(1);
            restaurant.setName("レストラン");
            restaurant.setCountries(countryOptional.get());
            restaurant.setThoughts("good");
            restaurant.setUrl("http://sample.com");

            // set mock
            when(countryRepositoryMock.findById(form.getCountryId())).thenReturn(countryOptional);
            when(restaurantRepositoryMock.save(any())).thenReturn(restaurant);

            // execute target method
            RestaurantResponse response = restaurantService.register(form);

            // assert response
            assertEquals(form.getCountryId(), response.getRestaurant().getCountries().getId());
            assertEquals(form.getName(), response.getRestaurant().getName());
        }
    }

}
