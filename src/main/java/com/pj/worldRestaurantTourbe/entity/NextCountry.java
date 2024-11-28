package com.pj.worldRestaurantTourbe.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pj.worldRestaurantTourbe.utils.BooleanDeserializer;
import lombok.Data;

@Data
public class NextCountry {

    private int id;

    @JsonDeserialize(using = BooleanDeserializer.class) // カスタムデシリアライザを使用
    private boolean next;

}
