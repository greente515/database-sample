package com.example.demo.controller;

import com.example.demo.service.FooService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FooController {

    private final FooService fooService;

    @PostMapping("/save")
    public void bar(String userId) {
        fooService.bar("test1");
    }

    @GetMapping("/get")
    public Map<String, Object> get(String userId) {
       return fooService.get(userId);
    }
}
