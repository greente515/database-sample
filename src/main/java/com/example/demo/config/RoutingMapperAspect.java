package com.example.demo.config;


import com.example.demo.annotation.AnnotationHandler;
import com.example.demo.annotation.RoutingKey;
import com.example.demo.annotation.RoutingMapper;
import com.example.demo.repository.CommonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

@Slf4j
@Aspect
@Configuration
@RequiredArgsConstructor
public class RoutingMapperAspect {

    private final Database database;
    private final static Integer RADIX = 24;

    public RoutingMapperAspect() {
        this.database = Database.HOME;
    }

    @Around("execution(* com.example.demo..repository..*Mapper.*(..))")
    public Object aroundTargetMethod(ProceedingJoinPoint thisJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
        log.debug("methodSignature : {}", methodSignature);
        Class<?> mapperInterface = methodSignature.getDeclaringType();
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = thisJoinPoint.getArgs();

        RoutingMapper routingMapper = mapperInterface.getDeclaredAnnotation(RoutingMapper.class);
        log.debug("routingMapper.database() : {}", routingMapper.database());
//        log.debug("routingMapper : {}", routingMapper.toString());

        if (routingMapper != null) {
            if (routingMapper.database() == Database.COMMON || routingMapper.database() == Database.VISIT || routingMapper.database() == Database.MINI) {
                ContextHolder.set(routingMapper.database().name());
            }
            if (routingMapper.database() == Database.HOME) {
                String userId = findRoutingKey(parameters, args);
                log.debug("userId : {}" , userId);
                String index = determineRoutingDataSourceIndex(userId);
                log.debug("index: {}", index);
                ContextHolder.set(index);
            }
        }


        try {
            log.debug("try in");
            return thisJoinPoint.proceed();
        } catch (Throwable throwable) {
            log.debug("catch in");
            throw new RuntimeException(throwable);
        } finally {
            log.debug("finally in");
            ContextHolder.clear();
        }
    }

    private String findRoutingKey(Parameter[] parameters, Object[] args) {
        for (int i = 0; i < parameters.length; i++) {
            RoutingKey routingKey = parameters[i].getDeclaredAnnotation(RoutingKey.class);
            log.debug("RoutingKey : {}", routingKey.toString());
            if (routingKey != null) {
                return String.valueOf(args[i]);
            }
        }
        throw new RuntimeException("can't find RoutingKey");
    }

    private String determineRoutingDataSourceIndex(String userId) {

        long tid = getTidDeciaml(userId);
        //HOME+server+db
        int serverNo = (int) (tid % 20); //serverNo
        log.debug("serverNo : {}", String.format("%02d", serverNo));
        int dbNo = (int) (tid % 200); //dbNo
        log.debug("dbNo : {}", String.format("%03d", dbNo));

        return String.format("%s%s%s", database.name(), String.format("%02d", serverNo), String.format("%03d", dbNo));
//        return DataSourceType.valueOf(String.format("%s%s", database.name(), serverNo));
    }

    private long getTidDeciaml(String targetId) {
        long total = 0L;

        if (targetId.matches("\\d+")) {
            //순수 숫자이면 변환 없다.
            total = Long.parseLong(targetId);
        } else {
            targetId = targetId.toUpperCase();
            String validValues = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            int lastIndex = targetId.length() - 1;
            byte[] letters = targetId.getBytes();
            for (int i = 0; i < letters.length; i++) {
                int actualValue = validValues.lastIndexOf(letters[i]);
                if (actualValue >= 0) {
                    //System.out.println(actualValue);
                    total += (long) ((actualValue + 1) * Math.pow(RADIX, lastIndex));
                    //System.out.println(total);
                }
                lastIndex--;
            }
        }
        return total;
    }

//    private Integer determineRoutingDataSourceIndex(String userId) {
////        log.debug("Math.abs(userId.hashCode()) % 4 : {}",Math.abs(userId.hashCode()) % 4);
////        return Math.abs(userId.hashCode()) % 4;
//
//        long total = 0L;
//
//        if (userId.matches("\\d+")) {
//            // 순수 숫자이면 변환 없다.
//            total = Long.parseLong(userId);
//        } else {
//            userId = userId.toUpperCase();
//            String validValues = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//            int lastIndex = userId.length() - 1;
//            byte[] letters = userId.getBytes();
//
//            for (int i = 0; i < letters.length; i++) {
//
//                int actualValue = validValues.lastIndexOf(letters[i]);
//                //System.out.println("letters["+i+"] : " + letters[i] + " : actualValue : " + actualValue);
//                if (actualValue >= 0) {
//                    // System.out.println(actualValue);
//                    total += (long) ((actualValue + 1) * Math.pow(24, lastIndex));
//                    // System.out.println(total);
//                }
//                lastIndex--;
//            }
//        }
//        int index = (int) (total % 20); //serverNo
//        log.debug("index : {}", index);
//        int dbNo = (int) (total % 200); //dbNo
//        log.debug("dbNo : {}", dbNo);
//
//        return dbNo;
//    }
}
