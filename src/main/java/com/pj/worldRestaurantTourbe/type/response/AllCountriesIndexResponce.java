package com.pj.worldRestaurantTourbe.type.response;

import com.pj.worldRestaurantTourbe.type.CountryItem;
import com.pj.worldRestaurantTourbe.type.entity.Countries;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllCountriesIndexResponce {

    List<CountryItem> countries;
}
