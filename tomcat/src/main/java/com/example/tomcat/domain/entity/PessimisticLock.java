package com.example.tomcat.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PessimisticLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "remain")
    private Long remain;

    public void decNum() {
        if (this.remain == 0) {
            throw new IllegalArgumentException("잔여석 없음");
        }
        this.remain -= 1;
    }
}
