package com.example.demo.service;

import com.example.demo.repository.CommonMapper;
import com.example.demo.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FooService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommonMapper commonMapper;

    @Transactional
    public void bar(String userId) {
        userMapper.insert(userId);
        commonMapper.insert(userId);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> get(String userId) {
        List<String> user = userMapper.get(userId);
        List<String> common = commonMapper.get(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("common", common);

        return map;
    }
}
