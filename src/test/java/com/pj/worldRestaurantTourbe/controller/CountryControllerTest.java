package com.pj.worldRestaurantTourbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pj.worldRestaurantTourbe.entity.Countries;
import com.pj.worldRestaurantTourbe.entity.NextCountry;
import com.pj.worldRestaurantTourbe.service.CountryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @BeforeAll
    public static void setup() {

        request = new MockHttpServletRequest();

        request.setParameter("id", "2");
        request.setParameter("name", "korea");
        request.setParameter("next", "true");
        request.setParameter("completed", "false");

    }

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddCountry);
    }

    @Test
    @DisplayName("getAllCountries")
    public void getAllCountriesHttpRequest() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/country/allCountries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("changeNextCountry")
    public void changeNextCountryHttpRequest() throws Exception{

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
}
