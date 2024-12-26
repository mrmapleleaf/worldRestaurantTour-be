package com.pj.worldRestaurantTourbe.type.response;

import com.pj.worldRestaurantTourbe.type.RestaurantItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllRestaurantsResponse {

    List<RestaurantItem> contents;
}
