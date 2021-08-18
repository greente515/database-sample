package com.example.demo.repository;

import com.example.demo.annotation.RoutingKey;
import com.example.demo.annotation.RoutingMapper;
import com.example.demo.config.Database;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@RoutingMapper(database = Database.VISIT)
@Mapper
public interface CommonMapper {

    @Insert("insert into common_test(userId) values(#{userId})")
    void insert(@Param("userId") String userId);

    @Select("select * from common_test(userId) where userId = #{userId}")
    List<String> get(@Param("userId") String userId);
}
