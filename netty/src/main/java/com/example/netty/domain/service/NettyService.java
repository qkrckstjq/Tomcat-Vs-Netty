package com.example.netty.domain.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class NettyService {
    public void calculate() {
        Mono.fromRunnable(() -> {
            long num = 0;
            for (long i = 0; i < 1000000000; i++) {
                num += i;
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
