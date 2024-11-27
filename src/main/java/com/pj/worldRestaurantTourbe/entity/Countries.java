package com.pj.worldRestaurantTourbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "countries")
public class Countries {

    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "next")
    private boolean next;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurants restaurants;

    @Column(name = "completed")
    private boolean completed;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;
}
