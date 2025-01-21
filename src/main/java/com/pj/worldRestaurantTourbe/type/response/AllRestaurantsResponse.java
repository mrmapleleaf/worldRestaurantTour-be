package com.pj.worldRestaurantTourbe.type.response;

import com.pj.worldRestaurantTourbe.type.RestaurantItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllRestaurantsResponse {

    List<RestaurantItem> contents;
}
