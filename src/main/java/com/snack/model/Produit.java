package com.snack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Produit() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

