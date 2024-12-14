package com.pj.worldRestaurantTourbe.type.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "restaurants")
public class Restaurants extends AbstractEntity{

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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Countries countries;
}
