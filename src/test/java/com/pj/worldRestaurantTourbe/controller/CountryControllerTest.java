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

    @Value("${sql.script.create.country1}")
    private String sqlAddCountry1;

    @Value("${sql.script.create.country2}")
    private String sqlAddCountry2;

    @Value("${sql.script.create.country3}")
    private String sqlAddCountry3;

    @Value("${sql.script.delete.country}")
    private String sqlDeleteCountry;

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
        jdbc.execute(sqlAddCountry1);
        jdbc.execute(sqlAddCountry2);
        jdbc.execute(sqlAddCountry3);
    }

    @Test
    @DisplayName("getAllCountries")
    public void getAllCountriesHttpRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/country/allCountries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.countries", hasSize(3)));
    }

    @Test
    @DisplayName("getNextCountry")
    public void getNextCountryHttpRequest() throws Exception {
        insertSecondRecord();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/country/nextCountry"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.nextCountry", hasSize(1)));
    }

    @Test
    @DisplayName("decideNextCountry")
    public void decideNextCountryHttpRequest() throws Exception {

        NextCountryResponse response = countryService.getNextCountry();
        assertEquals(0, response.getNextCountry().size());

        NextCountryForm nextCountryForm = new NextCountryForm();
        nextCountryForm.setId(1);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/country/decideNextCountry")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nextCountryForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("japan")))
                .andExpect(jsonPath("$.next", is(true)))
                .andExpect(jsonPath("$.completed", is(false)));

        NextCountryResponse nextCountries = countryService.getNextCountry();
        assertEquals(1, nextCountries.getNextCountry().size());
    }

    @Test
    @DisplayName("resetNextCountry")
    public void resetNextCountryHttpRequest() throws Exception {
        insertSecondRecord();

        NextCountryResponse nextCountries = countryService.getNextCountry();
        assertEquals(1, nextCountries.getNextCountry().size());

        NextCountryForm nextCountryForm = new NextCountryForm();
        nextCountryForm.setId(4);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/country/resetNextCountry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nextCountryForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("india")))
                .andExpect(jsonPath("$.next", is(false)))
                .andExpect(jsonPath("$.completed", is(false)));

        nextCountries = countryService.getNextCountry();
        assertEquals(0, nextCountries.getNextCountry().size());
    }

    @Test
    @DisplayName("setCompleted")
    public void setCompletedHttpRequest() throws Exception {
        insertSecondRecord();

        VisitedCountryForm visitedCountryForm = new VisitedCountryForm();
        visitedCountryForm.setId(4);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/country/setCompleted")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visitedCountryForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("india")))
                .andExpect(jsonPath("$.next", is(false)))
                .andExpect(jsonPath("$.completed", is(true)));
    }

    private void insertSecondRecord() {
        Countries country = new Countries();
        country.setId(4);
        country.setName("india");
        country.setNext(true);
        country.setCompleted(false);

        entityManager.persist(country);
        entityManager.flush();
    }

    @AfterEach
    public void cleanDatabase() {
        jdbc.execute(sqlDeleteCountry);
    }
}
