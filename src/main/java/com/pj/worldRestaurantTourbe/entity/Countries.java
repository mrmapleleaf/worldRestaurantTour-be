package com.pj.worldRestaurantTourbe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Countries {

    @Id
    private int id;

    private String name;

    private boolean is_next;

    private boolean is_completed;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;
}
