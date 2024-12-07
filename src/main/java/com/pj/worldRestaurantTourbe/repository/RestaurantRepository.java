package com.pj.worldRestaurantTourbe.repository;

import com.pj.worldRestaurantTourbe.type.entity.Restaurants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurants, Long> {

    // onetooneのカラムが、JPAの自動生成だとlazyfetchにならないので、jpqlを使う
    @Query("SELECT r FROM Restaurants r JOIN FETCH r.countries WHERE r.countries.id = :id")
    public Restaurants findByCountriesId(Long id);
}
