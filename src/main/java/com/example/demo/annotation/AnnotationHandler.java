package com.example.demo.annotation;

import java.lang.reflect.Field;
import java.util.Optional;

public class AnnotationHandler {

    /**
     * 전달된 클래스에 특정 어노테이션이 있는지 검사하여 값을 반환하는 메서드
     * @param targetClass 어노테이션 검사 대상 클래스
     * @param annotationClass 검사할 어노테이션
     * @return Optional로 감싸진 객체
     */
    //public <T> Optional<T> getInstance(Class targetClass, Class annotationClass) {
    public Optional<Object> getInstance(Class targetClass, Class annotationClass) {
        Optional<Object> optional = Optional.empty();
        Object object;
        try {
            object = targetClass.newInstance(); //대상 클래스의 객체를 생성해준다.
            object = checkAnnotation(object, annotationClass); //대상 클래스에서 어노테이션을 찾아서 주입
            optional = Optional.of(object); //옵셔널에 값을 대입
        }catch (InstantiationException | IllegalAccessException e) { System.out.println(e.getMessage()); }
        return optional;
    }



    /**
     * 어노테이션을 확인하여 값 주입을 수행하는 메서드
     * @param targetObj 대상 객체
     * @param annotationObj 어노테이션 객체
     * @param <T> 제너릭 객체 (대상 객체)
     * @return 주입 이후의 대상 객체를 반환
     */
    private <T> T checkAnnotation(T targetObj, Class annotationObj) {
        for (Field f : targetObj.getClass().getDeclaredFields()) { //선언된 필드를 가져온다.
            if (annotationObj == RoutingMapper.class) {
                return checkAnnotation4InsertEnum(targetObj, f);
            }
//            if(annotationObj == InsertIntData.class) { //정수형
//                return checkAnnotation4InsertInt(targetObj, f);
//            }
//            else if(annotationObj == InsertStringData.class) { //문자열
//                return checkAnnotation4InsertString(targetObj, f);
//            }
        }
        return targetObj;
    }

    private <T> T checkAnnotation4InsertEnum(T targetObj, Field field) {
        RoutingMapper annotation = field.getAnnotation(RoutingMapper.class);
        if (annotation != null && field.getType() == String.class) { //어노테이션이 null이 아니며 타입이 문자열인 경우 - enum
            field.setAccessible(true); //해당 필드의 접근을 허용
            try{
                field.set(targetObj, annotation.database().name());
            } catch(IllegalAccessException e){
                System.out.println(e.getMessage());
            }
        }

        return targetObj;
    }

    /**
     * 정수형 어노테이션을 확인하여 값을 주입하는 메서드
     * @param targetObj 대상 객체
     * @param field 대상 객체의 필드
     * @param <T> 제너릭 객체 (대상 객체)
     * @return 주입 이후의 대상 객체를 반환
     */
//    private <T> T checkAnnotation4InsertInt(T targetObj, Field field) {
//        InsertIntData annotation = field.getAnnotation(InsertIntData.class); //필드에 선언된 어노테이션을 가져온다.
//        if(annotation != null && field.getType() == int.class) { //어노테이션이 null이 아니며 타입이 정수형인 경우
//            field.setAccessible(true);  //해당 필드의 접근을 허용
//            try {  field.set(targetObj, annotation.data()); } //해당 필드에 어노테이션 값을 주입한다.
//            catch (IllegalAccessException e) { System.out.println(e.getMessage()); }
//        }
//        return targetObj;
//    }

    /**
     * 문자열 어노테이션을 확인하여 값을 주입하는 메서드
     * @param targetObj 대상 객체
     * @param field 대상 객체의 필드
     * @param <T> 제너릭 객체 (대상 객체)
     * @return 주입 이후의 대상 객체를 반환
     */
//    private <T> T checkAnnotation4InsertString(T targetObj, Field field) {
//        InsertStringData annotation = field.getAnnotation(InsertStringData.class); //필드에 선언된 어노테이션을 가져온다.
//        if(annotation != null && field.getType() == String.class) { //어노테이션이 null이 아니며 타입이 문자열인 경우
//            field.setAccessible(true); //해당 필드의 접근을 허용
//            try { field.set(targetObj, annotation.data()); } //해당 필드에 어노테이션 값을 주입한다.
//            catch (IllegalAccessException e) { System.out.println(e.getMessage()); }
//        }
//        return targetObj;
//    }
}
