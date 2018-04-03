package com.kaishengit.dao;

import javax.inject.Named;

@Named  //JSR330规范中的注解  实现bean管理
public class UserDao {

    public void save() {
        System.out.println("userDao save...");
    }
}
