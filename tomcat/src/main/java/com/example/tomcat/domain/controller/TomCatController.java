package com.example.tomcat.domain.controller;

import com.example.tomcat.domain.service.TomCatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TomCatController {
    private final TomCatService tomCatService;
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/calculator")
    public String calculate() {
        tomCatService.calculate();
        return "Calculate done";
    }

    @PostMapping("/post")
    public String post(
            @RequestBody String data
    ) {
        return tomCatService.post(data);
    }

    @GetMapping("/get")
    public String get() {
        return tomCatService.get();
    }
}
