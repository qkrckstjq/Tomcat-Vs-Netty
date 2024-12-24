package com.example.tomcat.domain.service;

import com.example.tomcat.domain.entity.JustText;
import com.example.tomcat.domain.entity.PessimisticLock;
import com.example.tomcat.domain.repository.JustTextRepository;
import com.example.tomcat.domain.repository.PessimisticLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TomCatService {
    private final JustTextRepository justTextRepository;
    private final PessimisticLockRepository pessimisticLockRepository;

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

    @Transactional
    public String lock() {
//        PessimisticLock pessimisticLock = pessimisticLockRepository.findById(1L).orElseThrow(() ->
//                new IllegalArgumentException("없음")
//        );
        PessimisticLock pessimisticLock = pessimisticLockRepository.findByIdWithPessimistic(1L).orElseThrow(() ->
                new IllegalArgumentException("없음")
        );
        Long remainNum = pessimisticLock.getRemain();
        pessimisticLock.decNum();
        return remainNum + 1 + "번 예약 성공";
    }
}
