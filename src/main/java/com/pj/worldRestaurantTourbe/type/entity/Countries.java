package com.pj.worldRestaurantTourbe.type.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "countries")
public class Countries extends AbstractEntity{

    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "next")
    private boolean next;

    @Column(name = "restaurant_id")
    private Integer restaurant_id;

    @Column(name = "completed")
    private boolean completed;

}
