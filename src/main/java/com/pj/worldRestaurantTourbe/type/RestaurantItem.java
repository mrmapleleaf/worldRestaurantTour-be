package com.pj.worldRestaurantTourbe.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pj.worldRestaurantTourbe.type.entity.Countries;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantItem {

    private int id;

    private String name;

    private String thoughts;

    private String url;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Countries countries;
}
