package com.kaishengit.dao;

import com.kaishengit.BaseTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoTest extends BaseTestCase {

    @Autowired   //Spring自带的  自动注入
    private UserDao userDao;

    @Test
    public void save() {
        userDao.save();
    }

}