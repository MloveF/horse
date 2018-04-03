package com.kaishengit.service;

import com.kaishengit.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service  //实现bean管理
public class UserService {

    @Autowired  //Spring自带的  自动注入
    private UserDao userDao;

    public void save() {
        userDao.save();
    }
}
