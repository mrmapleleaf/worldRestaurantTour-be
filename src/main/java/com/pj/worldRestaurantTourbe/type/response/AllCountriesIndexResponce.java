package com.pj.worldRestaurantTourbe.type.response;

import com.pj.worldRestaurantTourbe.type.entity.Countries;
import lombok.Data;

import java.util.List;

@Data
public class AllCountriesIndexResponce {

    List<Countries> countries;
}
