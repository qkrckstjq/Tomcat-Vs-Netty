package com.example.tomcat.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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
