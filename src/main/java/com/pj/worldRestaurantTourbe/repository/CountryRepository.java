package com.pj.worldRestaurantTourbe.repository;

import com.pj.worldRestaurantTourbe.type.entity.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Countries, Long>{

    public List<Countries> findByNextIs(boolean next);
}
