package com.pj.worldRestaurantTourbe.type.response;

import com.pj.worldRestaurantTourbe.type.CountryItem;
import lombok.Data;

import java.util.List;

@Data
public class NextCountryResponse {

    List<CountryItem> nextCountry;
}
