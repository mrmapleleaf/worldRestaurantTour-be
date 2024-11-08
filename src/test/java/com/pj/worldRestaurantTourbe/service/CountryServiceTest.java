package com.pj.worldRestaurantTourbe.service;

import com.pj.worldRestaurantTourbe.WorldRestaurantTourBeApplication;
import com.pj.worldRestaurantTourbe.entity.Countries;
import com.pj.worldRestaurantTourbe.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        // when(countryRepository.findAll()).thenReturn(new ArrayList<>(Collections.singletonList(country)));
        doReturn(new ArrayList<>(Collections.singletonList(country))).when(countryRepository).findAll();

        assertNotNull(countryService.getAllCountries(), "country should not be null");
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("get next country")
    public void getNextCountryTest() {
        boolean isNext = true;

        ReflectionTestUtils.setField(country, "next", true);

        doReturn(new ArrayList<>(Collections.singletonList(country))).when(countryRepository).findByNextIs(isNext);

        assertNotNull(countryService.getNextCountry(isNext), "country should not be null");
        assertTrue(country.isNext());
        verify(countryRepository, times(1)).findByNextIs(isNext);
    }

    @Test
    @DisplayName("success updating next country")
    public void testUpdateIsNext_whenCountryExists_updateToTrue() {
        long id = 1L;
        boolean isNext = true;

        doReturn(Optional.of(country)).when(countryRepository).findById((long)country.getId());

        assertNotNull(countryService.updateIsNext(id, isNext), "country should not be null");
        assertTrue(country.isNext());
        assertNotNull(country.getUpdated_at());
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    @DisplayName("fail updating next country")
    public void testUpdateIsNext_whenCountryDoesNotExist_throwException() {
        long id = 999L;
        boolean isNext = false;

        doThrow(new RuntimeException("更新対象の国が存在しません")).when(countryRepository).findById(id);

        assertThrows(RuntimeException.class, () -> { countryService.updateIsNext(id, true);});
        assertFalse(country.isNext());
    }

    @Test
    @DisplayName("success updating completed")
    public void testUpdateCompleted_whenCountryExists_updateToTrue() {
        long id = 1L;
        boolean isNext = false;
        boolean completed = true;

        ReflectionTestUtils.setField(country, "next", true);
        doReturn(Optional.of(country)).when(countryRepository).findById((long)country.getId());

        assertNotNull(countryService.updateCompleted(id, isNext, completed), "country should not be null");
        assertFalse(country.isNext());
        assertTrue(country.isCompleted());
        assertNotNull(country.getUpdated_at());
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    @DisplayName("fail updating completed")
    public void testUpdateCompleted_whenCountryDoesNotExist_throwException() {
        long id = 999L;
        boolean isNext = false;
        boolean completed = true;

        ReflectionTestUtils.setField(country, "next", true);
        doThrow(new RuntimeException("更新対象の国が存在しません")).when(countryRepository).findById(id);

        assertThrows(RuntimeException.class, () -> { countryService.updateCompleted(id, isNext, completed);});
        assertTrue(country.isNext());
        assertFalse(country.isCompleted());
        assertNull(country.getUpdated_at());
    }

}
