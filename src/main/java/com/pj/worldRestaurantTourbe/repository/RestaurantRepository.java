package com.pj.worldRestaurantTourbe.repository;

import com.pj.worldRestaurantTourbe.type.entity.Restaurants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurants, Long> {

}
