package com.pj.worldRestaurantTourbe.repository;

import com.pj.worldRestaurantTourbe.entity.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Countries, Long>{

}
