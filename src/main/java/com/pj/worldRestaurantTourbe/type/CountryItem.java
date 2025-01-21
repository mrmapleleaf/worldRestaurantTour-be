package com.pj.worldRestaurantTourbe.type;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryItem {

    private int id;

    private String name;

    private boolean next;

    private boolean completed;

}
