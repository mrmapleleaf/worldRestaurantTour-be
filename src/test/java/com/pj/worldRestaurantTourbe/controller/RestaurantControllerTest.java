package com.pj.worldRestaurantTourbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pj.worldRestaurantTourbe.type.form.CompletedRestaurantFrom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
public class RestaurantControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${sql.script.create.country4}")
    private String sqlAddCountry4;

    @Value("${sql.script.create.country5}")
    private String sqlAddCountry5;

    @Value("${sql.script.delete.country}")
    private String sqlDeleteCountry;

    @Value("${sql.script.create.restaurant}")
    private String sqlAddRestaurant;

    @Value("${sql.script.delete.restaurant}")
    private String sqlDeleteRestaurant;

    private static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setupDatabase() {
            jdbc.execute(sqlAddCountry4);
            jdbc.execute(sqlAddCountry5);
            jdbc.execute(sqlAddRestaurant);
    }

    @Test
    @DisplayName("getAllRestaurants")
    public void getAllRestaurantsHttpRequest() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/allRestaurants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.contents", hasSize(1)));
    }

    @Test
    @DisplayName("registerRestaurant")
    public void registerRestaurantHttpRequest() throws Exception{

        CompletedRestaurantFrom form = new CompletedRestaurantFrom();
        form.setCountryId(2);
        form.setName("レストラン");
        form.setThoughts("good");
        form.setUrl("http://sample.com");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/register")
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("レストラン")))
                .andExpect(jsonPath("$.thoughts", is("good")))
                .andExpect(jsonPath("$.url", is("http://sample.com")))
                .andExpect(jsonPath("$.countries.id", is(2)));
    }



    // 同じ国に対して２つレストランを登録する
    @Test
    @DisplayName("registerTwoRestaurantsForTheSameCountry")
    public void registerTwoRestaurantsForTheSameCountryHttpRequest() throws Exception{
        
    }

    @Test
    @DisplayName("getRestaurantDetail")
    public void getRestaurantDetailHttpRequest() throws Exception {
        int restaurantId = 2;

       this.mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/detail/{restaurant}", restaurantId)
               .contentType(APPLICATION_JSON_UTF8))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.name", is("restaurant")))
               .andExpect(jsonPath("$.thoughts", is("good")))
               .andExpect(jsonPath("$.url", is("http://sample.com")))
               .andExpect(jsonPath("$.countries.id", is(1)));
    }

    @Test
    @DisplayName("deleteRestaurant")
    public void deleteRestaurantHttpRequest() throws Exception {
        int restaurantId = 2;

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/restaurant/delete/{restaurantId}", restaurantId)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("restaurant")))
                .andExpect(jsonPath("$.thoughts", is("good")))
                .andExpect(jsonPath("$.url", is("http://sample.com")))
                .andExpect(jsonPath("$.countries.id", is(1)));
    }

    @AfterEach
    public void deleteDatabase() {
        jdbc.execute(sqlDeleteRestaurant);
        jdbc.execute(sqlDeleteCountry);
    }
}
