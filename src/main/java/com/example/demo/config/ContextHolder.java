package com.example.demo.config;

public class ContextHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void set(String index) {
        CONTEXT.set(index);
    }

    public static String get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
