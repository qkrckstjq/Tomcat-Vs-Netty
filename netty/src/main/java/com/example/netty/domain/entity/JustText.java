package com.example.netty.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("just_text") // 데이터베이스 테이블 이름
@Getter
public class JustText {

    @Id
    private Long id;

    private String justText; // 컬럼은 기본적으로 필드 이름과 매핑

    public JustText(String justText) {
        this.justText = justText;
    }

    public JustText() {}
}
