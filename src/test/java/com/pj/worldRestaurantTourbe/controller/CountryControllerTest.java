package com.pj.worldRestaurantTourbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pj.worldRestaurantTourbe.type.entity.Countries;
import com.pj.worldRestaurantTourbe.service.CountryService;
import com.pj.worldRestaurantTourbe.type.form.NextCountryForm;
import com.pj.worldRestaurantTourbe.type.form.VisitedCountryForm;
import com.pj.worldRestaurantTourbe.type.response.NextCountryResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc

// EntityManagerを使うため
@Transactional
public class CountryControllerTest {

    @Value("${sql.script.create.country}")
    private String sqlAddCountry;

    @Value("${sql.script.delete.country}")
    private String sqlDeleteAddedCountry;

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
                .andExpect(jsonPath("$.countries", hasSize(1)));
    }

    @Test
    @DisplayName("getNextCountry")
    public void getNextCountryHttpRequest() throws Exception {
        insertSecondRecord();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/country/nextCountry")
                        .param("next", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.nextCountry", hasSize(1)));
    }

    @Test
    @DisplayName("decideNextCountry")
    public void decideNextCountryHttpRequest() throws Exception {

        NextCountryResponse response = countryService.getNextCountry(true);
        assertEquals(0, response.getNextCountry().size());

        NextCountryForm nextCountryForm = new NextCountryForm();
        nextCountryForm.setId(1);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/country/decideNextCountry")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nextCountryForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.country.id", is(1)))
                .andExpect(jsonPath("$.country.name", is("japan")))
                .andExpect(jsonPath("$.country.next", is(true)))
                .andExpect(jsonPath("$.country.completed", is(false)));

        NextCountryResponse nextCountries = countryService.getNextCountry(true);
        assertEquals(1, nextCountries.getNextCountry().size());
    }

    @Test
    @DisplayName("resetNextCountry")
    public void resetNextCountryHttpRequest() throws Exception {
        insertSecondRecord();

        NextCountryResponse nextCountries = countryService.getNextCountry(true);
        assertEquals(1, nextCountries.getNextCountry().size());

        NextCountryForm nextCountryForm = new NextCountryForm();
        nextCountryForm.setId(2);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/country/resetNextCountry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nextCountryForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.country.id", is(2)))
                .andExpect(jsonPath("$.country.name", is("korea")))
                .andExpect(jsonPath("$.country.next", is(false)))
                .andExpect(jsonPath("$.country.completed", is(false)));

        nextCountries = countryService.getNextCountry(true);
        assertEquals(0, nextCountries.getNextCountry().size());
    }

    @Test
    @DisplayName("setCompleted")
    public void setCompletedHttpRequest() throws Exception {

        VisitedCountryForm visitedCountryForm = new VisitedCountryForm();
        visitedCountryForm.setId(1);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/country/setCompleted")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visitedCountryForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country.id", is(1)))
                .andExpect(jsonPath("$.country.name", is("japan")))
                .andExpect(jsonPath("$.country.next", is(false)))
                .andExpect(jsonPath("$.country.completed", is(true)));
    }

    private void insertSecondRecord() {
        Countries country = new Countries();
        country.setId(2);
        country.setName("korea");
        country.setNext(true);
        country.setCompleted(false);

        entityManager.persist(country);
        entityManager.flush();
    }

    @AfterEach
    public void cleanDatabase() {
        jdbc.execute(sqlDeleteAddedCountry);
    }
}
