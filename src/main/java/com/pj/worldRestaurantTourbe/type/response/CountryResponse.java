package com.pj.worldRestaurantTourbe.type.response;

import com.pj.worldRestaurantTourbe.type.entity.Countries;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryResponse {

    private int id;

    private String name;

    private boolean next;

    private boolean completed;
}
