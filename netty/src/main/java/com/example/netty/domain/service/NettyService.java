package com.example.netty.domain.service;

import com.example.netty.domain.entity.JustText;
import com.example.netty.domain.repository.JustTextRepository;
import com.example.netty.domain.repository.PessimisticLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NettyService {
    private final JustTextRepository justTextRepository;
    private final PessimisticLockRepository pessimisticLockRepository;
    private final TransactionalOperator transactionalOperator;

    public void calculate() {
        Mono.fromRunnable(() -> {
            long num = 0;
            for (long i = 0; i < 1000000000; i++) {
                num += i;
            }
        });
//        long num = 0;
//        for (long i = 0; i < 1000000000; i++) {
//            num += i;
//        }
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

    public Mono<String> lock() {
        return transactionalOperator.execute(transactionStatus ->
            pessimisticLockRepository.findByIdWithPessimistic(1L)
                    .switchIfEmpty(Mono.error(new IllegalArgumentException("없음")))
                    .flatMap(lock -> {
                        Long remainNum = lock.getRemain();
                        lock.decNum();
                        return pessimisticLockRepository.save(lock).thenReturn(remainNum + 1 + "번 예약 성공");
                    })
        ).single();
    }
}
