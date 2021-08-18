package com.example.demo.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Database {

    HOME(0),
    COMMON(1),
    VISIT(2),
    MINI(3);

    private final Integer value;
}
