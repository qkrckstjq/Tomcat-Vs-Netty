package com.example.netty.domain.service;

import com.example.netty.domain.entity.JustText;
import com.example.netty.domain.repository.JustTextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NettyService {
    private final JustTextRepository justTextRepository;

    public void calculate() {
        Mono.fromRunnable(() -> {
            long num = 0;
            for (long i = 0; i < 1000000000; i++) {
                num += i;
            }
        });
    }

    public Mono<String> post(String data) {
        JustText jst = new JustText(data);
        return justTextRepository.save(jst)
                .map(JustText::getJustText);
    }

    public Mono<String> get() {
        return justTextRepository.findById(1L)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("없음")))
                .map(JustText::getJustText);
    }
}
