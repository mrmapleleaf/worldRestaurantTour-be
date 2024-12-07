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

    @Column(name = "created_at", columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    @Column(name = "updated_at", columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updated_at;

    @PrePersist
    public void onPrePersist() {
        this.setCreated_at(LocalDateTime.now());
        this.setUpdated_at(LocalDateTime.now());
    }
}
