package com.pj.worldRestaurantTourbe.repository;

import com.pj.worldRestaurantTourbe.type.entity.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Countries, Integer>{

    @Query(value = "select * from Countries where Countries.next = true", nativeQuery = true)
    public List<Countries> findByNextIs();
}
