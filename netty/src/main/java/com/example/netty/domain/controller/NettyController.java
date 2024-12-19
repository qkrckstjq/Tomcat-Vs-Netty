package com.example.netty.domain.controller;

import com.example.netty.domain.service.NettyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NettyController {
    private final NettyService nettyService;
    @GetMapping("/ping")
    public Mono<String> ping() {
        return Mono.just("pong");
    }

    @GetMapping("/calculator")
    public Mono<String> calculate() {
        nettyService.calculate();
        return Mono.just("calculate done");
    }
}
