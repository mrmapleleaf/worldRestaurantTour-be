package com.pj.worldRestaurantTourbe.service;

import com.pj.worldRestaurantTourbe.type.entity.Countries;
import com.pj.worldRestaurantTourbe.repository.CountryRepository;
import com.pj.worldRestaurantTourbe.type.error.CountryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = CountryService.class)
class CountryServiceTest {

    @Autowired
    CountryService countryService;

    @MockBean
    CountryRepository countryRepository;

    Countries country;

    @BeforeEach
    public void  setup() {
        country = new Countries();
        country.setId(1);
        country.setName("日本");
        country.setNext(false);
        country.setCompleted(false);
    }

    @Test
    @DisplayName("get all countries")
    public void getAllCountriesTest() {

        doReturn(new ArrayList<>(Collections.singletonList(country))).when(countryRepository).findAll();

        assertNotNull(countryService.getAllCountries().getCountries(), "country should not be null");
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("get next country")
    public void getNextCountryTest() {

        ReflectionTestUtils.setField(country, "next", true);

        doReturn(new ArrayList<>(Collections.singletonList(country))).when(countryRepository).findByNextIs();

        assertNotNull(countryService.getNextCountry(), "country should not be null");
        assertTrue(country.isNext());
        verify(countryRepository, times(1)).findByNextIs();
    }

    @Test
    @DisplayName("success setting next country")
    public void testSetNextCountryWhenCountryExists() {
        int id = 1;

        doReturn(Optional.of(country)).when(countryRepository).findById((country.getId()));

        assertNotNull(countryService.setNextCountry(id), "country should not be null");
        assertTrue(country.isNext());
        assertNotNull(country.getUpdated_at());
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    @DisplayName("fail setting next country")
    public void testSetNextCountryWhenCountryDoesNotExistThrowException() {
        int id = 999;

        doThrow(new CountryNotFoundException("更新対象の国が存在しません")).when(countryRepository).findById(id);

        assertThrows(CountryNotFoundException.class, () -> { countryService.setNextCountry(id);});
        assertFalse(country.isNext());
    }

    @Test
    @DisplayName("success unsetting next country")
    public void testUnsetNextCountryWhenCountryExists() {
        int id = 1;
        country.setNext(true);

        doReturn(Optional.of(country)).when(countryRepository).findById(country.getId());

        assertNotNull(countryService.unsetNextCountry(id), "country should not be null");
        assertFalse(country.isNext());
        assertNotNull(country.getUpdated_at());
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    @DisplayName("fail unsetting next country")
    public void testUnsetNextCountryWhenCountryDoesNotExistThrowException() {
        int id = 999;
        country.setNext(true);

        doThrow(new CountryNotFoundException("更新対象の国が存在しません")).when(countryRepository).findById(id);

        assertThrows(CountryNotFoundException.class, () -> { countryService.unsetNextCountry(id);});
        assertTrue(country.isNext());
    }

    @Test
    @DisplayName("success setting completed")
    public void testUpdateCompletedWhenCountryExistsUpdateToTrue() {
        int id = 1;

        ReflectionTestUtils.setField(country, "next", true);
        doReturn(Optional.of(country)).when(countryRepository).findById(country.getId());

        assertNotNull(countryService.setCompleted(id), "country should not be null");
        assertFalse(country.isNext());
        assertTrue(country.isCompleted());
        assertNotNull(country.getUpdated_at());
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    @DisplayName("fail updating completed")
    public void testUpdateCompleted_whenCountryDoesNotExist_throwException() {
        int id = 999;

        ReflectionTestUtils.setField(country, "next", true);
        doThrow(new CountryNotFoundException("更新対象の国が存在しません")).when(countryRepository).findById(id);

        assertThrows(CountryNotFoundException.class, () -> { countryService.setCompleted(id);});
        assertTrue(country.isNext());
        assertFalse(country.isCompleted());
        assertNull(country.getUpdated_at());
    }

}
