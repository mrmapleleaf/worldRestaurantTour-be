package com.pj.worldRestaurantTourbe.type.error;

public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException(String message) {
        super(message);
    }
}
