package com.kaishengit;


import com.kaishengit.dao.UserDao;
import com.kaishengit.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  //就是一个运行器  让测试运行于Spring测试环境
@ContextConfiguration(classes = Application.class)   //指定java配置类
public class ApplicationTest {

    @Autowired   //Spring自带的  自动注入
    /*private UserDao userDao;*/
    private UserService userService;

    @Test
    public void test() {
        userService.save();
    }
}
