package com.kaishengit.service;

import com.kaishengit.BaseTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends BaseTestCase {

    @Autowired   //Spring自带的  自动注入
    private UserService userService;

    @Test
    public void save() {
        userService.save();
    }
}
