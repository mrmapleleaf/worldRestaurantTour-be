package com.pj.worldRestaurantTourbe.type.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "restaurants")
public class Restaurants {

    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "thoughts")
    private String thoughts;

    @Column(name = "url")
    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Countries countries;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;
}
