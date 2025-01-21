package com.pj.worldRestaurantTourbe.service;

import com.pj.worldRestaurantTourbe.repository.CountryRepository;
import com.pj.worldRestaurantTourbe.repository.RestaurantRepository;
import com.pj.worldRestaurantTourbe.type.RestaurantItem;
import com.pj.worldRestaurantTourbe.type.entity.Countries;
import com.pj.worldRestaurantTourbe.type.entity.Restaurants;
import com.pj.worldRestaurantTourbe.type.error.RestaurantNotFoundException;
import com.pj.worldRestaurantTourbe.type.form.CompletedRestaurantFrom;
import com.pj.worldRestaurantTourbe.type.response.AllRestaurantsResponse;
import com.pj.worldRestaurantTourbe.type.response.RestaurantDetailResponse;
import com.pj.worldRestaurantTourbe.type.response.RestaurantResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
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
        public void registerRestaurant() {

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
            when(restaurantRepositoryMock.save(restaurant)).thenReturn(restaurant);

            // execute target method
            RestaurantResponse response = restaurantService.register(form);

            // assert response
            assertEquals(form.getCountryId(), response.getCountries().getId());
            assertEquals(form.getName(), response.getName());

            // verify method invocations
            verify(countryRepositoryMock).findById(form.getCountryId());
            verify(restaurantRepositoryMock).save(restaurant);
        }
    }

    @Nested
    @DisplayName("レストラン詳細取得")
    class GetRestaurant {
        @Test
        @DisplayName("対象のレストランが存在する場合")
        public void testGetRestaurantDetailWhenTargetRestaurantExists() {

            // prepare test data
            int restaurantId = 1;

            Restaurants restaurant = new Restaurants();
            Countries country = new Countries();
            country.setId(restaurantId);

            Optional<Countries> countryOptional = Optional.of(country);
            restaurant.setId(1);
            restaurant.setName("レストラン");
            restaurant.setCountries(countryOptional.get());
            restaurant.setThoughts("good");
            restaurant.setUrl("http://sample.com");

            // set mock
            when(restaurantRepositoryMock.findById(1)).thenReturn(Optional.of(restaurant));

            // execute target method
            RestaurantDetailResponse response = restaurantService.detail(restaurantId);

            // assert response
            assertEquals(1, restaurant.getId());
            assertEquals(1, restaurant.getCountries().getId());

            // verify method invocation
            verify(restaurantRepositoryMock).findById(restaurantId);
        }

        @Test
        @DisplayName("対象のレストランが存在しない場合")
        public void testGetRestaurantDetailWhenTargetRestaurantDoesNotExistThrowException() {

            // prepare test date
            int countryId = 999;

            // set mock
            when(restaurantRepositoryMock.findByCountriesId(countryId)).thenThrow(
                    new RestaurantNotFoundException("対象のレストランが見つかりません")
            );

            // execute target method and assert result
            assertThrows(RestaurantNotFoundException.class, () -> {
                    restaurantService.detail(countryId);
                }
            );

            // verify method invocation
            verify(restaurantRepositoryMock).findById(countryId);
        }
    }

    @Nested
    @DisplayName("レストラン削除")
    class DeleteRestaurant {

        @Test
        @DisplayName("削除が成功する場合")
        public void deleteRestaurantWhenTargetRestaurantExists() {

            // prepare test data
            int id = 1;

            Restaurants targetRestaurant = new Restaurants();
            targetRestaurant.setId(id);
            targetRestaurant.setName("レストラン");
            targetRestaurant.setThoughts("good");
            targetRestaurant.setUrl("http://sample.com");
            Optional<Restaurants> optionalRestaurant = Optional.of(targetRestaurant);

            // set mock
            when(restaurantRepositoryMock.findById(id)).thenReturn(optionalRestaurant);

            // execute target method
            RestaurantResponse response = restaurantService.delete(id);

            // assert response
            assertEquals(targetRestaurant.getId(), response.getId());

            // verify method invocation
            verify(restaurantRepositoryMock).findById(id);
            verify(restaurantRepositoryMock).delete(argThat(x -> targetRestaurant.getId() == x.getId()));
        }

        @Test
        @DisplayName("削除対象のレストランが存在しない場合")
        public void testDeleteRestaurantWhenTargetRestaurantDoesNotExistThrowException() {

            // prepare test data
            int id = 999;

            // set mock
            when(restaurantRepositoryMock.findById(id)).thenThrow(
                    RestaurantNotFoundException.class
            );

            // execute target method and assert result
            assertThrows(RestaurantNotFoundException.class, () -> restaurantService.delete(id));

            // verify method invocation
            verify(restaurantRepositoryMock).findById(id);
        }
    }

    @Nested
    @DisplayName("全レストラン取得")
    class GetAllRestaurant {
        @Test
        @DisplayName("レストラン一覧取得が成功する場合")
        public void testGetAllRestaurant() {

            // prepare test data
            Restaurants targetRestaurant = new Restaurants();
            targetRestaurant.setId(1);
            targetRestaurant.setName("レストラン");
            targetRestaurant.setThoughts("good");
            targetRestaurant.setUrl("http://sample.com");

            List<Restaurants> restaurantsList = new ArrayList<>();
            restaurantsList.add(targetRestaurant);

            // set mock
            when(restaurantRepositoryMock.findAll()).thenReturn(restaurantsList);

            // execute target method
            AllRestaurantsResponse response = restaurantService.getAllRestaurant();

            // asser response
            assertEquals(restaurantsList.size(), response.getContents().size());

            // verify method invocations
            verify(restaurantRepositoryMock).findAll();
        }
    }
}
