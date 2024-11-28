package com.pj.worldRestaurantTourbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pj.worldRestaurantTourbe.entity.Countries;
import com.pj.worldRestaurantTourbe.entity.NextCountry;
import com.pj.worldRestaurantTourbe.entity.VisitedCountry;
import com.pj.worldRestaurantTourbe.service.CountryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc

// EntityManagerを使うため
@Transactional
public class CountryControllerTest {

    @Value("${sql.script.create.country}")
    private String sqlAddCountry;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private CountryService countryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    private static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddCountry);
    }

    @Test
    @DisplayName("getAllCountries")
    public void getAllCountriesHttpRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/country/allCountries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("getNextCountry")
    public void getNextCountryHttpRequest() throws Exception {
        Countries country = new Countries();
        country.setId(2);
        country.setName("korea");
        country.setNext(true);
        country.setCompleted(false);

        entityManager.persist(country);
        entityManager.flush();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/country/nextCountry")
                        .param("next", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("changeNextCountry")
    public void changeNextCountryHttpRequest() throws Exception {

        List<Countries> nextCountries = countryService.getNextCountry(true);
        assertEquals(0, nextCountries.size());

        NextCountry nextCountry = new NextCountry();
        nextCountry.setId(1);
        nextCountry.setNext(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/country/changeNextCountry")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nextCountry)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("japan")))
                .andExpect(jsonPath("$.next", is(true)))
                .andExpect(jsonPath("$.completed", is(false)));

        nextCountries = countryService.getNextCountry(true);
        assertEquals(1, nextCountries.size());
    }

    @Test
    @DisplayName("makeChosenCountryCompleted")
    public void makeChosenCountryCompletedHttpRequest() throws Exception {

        VisitedCountry visitedCountry = new VisitedCountry();
        visitedCountry.setId(1);
        visitedCountry.setNext(false);
        visitedCountry.setCompleted(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/country/makeChosenCountryCompleted")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visitedCountry)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("japan")))
                .andExpect(jsonPath("$.next", is(false)))
                .andExpect(jsonPath("$.completed", is(true)));
    }
}
