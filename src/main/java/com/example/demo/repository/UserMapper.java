package com.example.demo.repository;

import com.example.demo.annotation.RoutingKey;
import com.example.demo.annotation.RoutingMapper;
import com.example.demo.config.Database;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@RoutingMapper
@Mapper
public interface UserMapper {

    @Insert("insert into user_test(userId) values(#{userId})")
    void insert(@RoutingKey @Param("userId") String userId);

    @Select("select * from user_test(userId) where user_id = #{userId}")
    List<String> get(@Param("userId") String userId);
}
