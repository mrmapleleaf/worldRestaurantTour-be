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

    @Value("${sql.script.create.country}")
    private String sqlAddCountry;

    @Value("${sql.script.delete.country}")
    private String sqlDeleteAddedCountry;

    @Value("${sql.script.delete.restaurant}")
    private String sqlDeleteAddedRestaurant;

    private static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddCountry);
    }

    @Test
    @DisplayName("register")
    public void registerRestaurantHttpRequest() throws Exception{
        CompletedRestaurantFrom form = new CompletedRestaurantFrom();
        form.setCountryId(1);
        form.setName("レストラン");
        form.setThoughts("good");
        form.setUrl("http://sample.com");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/register")
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.restaurant.name", is("レストラン")))
                .andExpect(jsonPath("$.restaurant.thoughts", is("good")))
                .andExpect(jsonPath("$.restaurant.url", is("http://sample.com")))
                .andExpect(jsonPath("$.restaurant.countries.id", is(1)));
    }

    @AfterEach
    public void deleteDatabase() {
        jdbc.execute(sqlDeleteAddedCountry);
        jdbc.execute(sqlDeleteAddedRestaurant);
    }
}
