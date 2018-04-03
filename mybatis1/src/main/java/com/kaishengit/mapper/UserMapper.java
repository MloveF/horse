package com.kaishengit.mapper;

import com.kaishengit.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface  UserMapper {

    int save(User user);
    User findById(int id);
    List<User> findAll();
    List<User> page(Map<String, Integer> map);
    List<User> page2(int start, int size);
    List<User> page3(@Param("start") int start, @Param("size") int size);
    int update(User user);
    int delById(int id);

}
