package com.example.demo.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return ContextHolder.get();
//        DbRouteKey routeKey = ContextHolder.get();
//
//        if (routeKey == null) {
//            routeKey = new DbRouteKey();
//        }
//
//        return routeKey.connectionKey();
    }
}
