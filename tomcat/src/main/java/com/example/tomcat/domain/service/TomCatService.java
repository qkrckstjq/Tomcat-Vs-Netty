package com.example.tomcat.domain.service;

import com.example.tomcat.domain.entity.JustText;
import com.example.tomcat.domain.repository.JustTextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TomCatService {
    private final JustTextRepository justTextRepository;

    public void calculate() {
        long num = 0;
        for(long i = 0; i < 1000000000; i++) {
            num += i;
        }
    }

    public String post(String data) {
        JustText jst = new JustText(data);
        return justTextRepository.save(jst).getJustText();
    }

    public String get() {
        JustText justText = justTextRepository.findById(1L).orElseThrow(() ->
                new IllegalArgumentException("없음")
        );
        return justText.getJustText();
    }
}
