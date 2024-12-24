package com.example.netty.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("pessimistic_lock")
@Getter
public class PessimisticLock {
    @Id
    private Long id;

    private Long remain;

    public void decNum() {
        if (this.remain == 0) {
            throw new IllegalArgumentException("잔여석 없음");
        }
        this.remain -= 1;
    }
}
