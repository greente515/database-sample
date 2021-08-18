package com.example.demo.annotation;

import com.example.demo.config.Database;

import java.lang.annotation.*;

@Inherited //하위클래스에 적용
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoutingMapper {
    Database database() default Database.HOME;
}
