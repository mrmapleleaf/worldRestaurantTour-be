package com.pj.worldRestaurantTourbe.type.response;

import com.pj.worldRestaurantTourbe.type.entity.Countries;
import lombok.Data;

import java.util.List;

@Data
public class NextCountryResponse {

    List<Countries> nextCountry;
}
