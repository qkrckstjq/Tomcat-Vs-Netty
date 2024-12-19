package com.example.tomcat.domain.entity;

import jakarta.persistence.*;

@Entity
public class JustText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String justText;

    public JustText (String justText) {
        this.justText = justText;
    }
}
