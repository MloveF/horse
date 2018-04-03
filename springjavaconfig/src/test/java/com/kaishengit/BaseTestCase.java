package com.kaishengit;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) //就是一个运行器  让测试运行于Spring测试环境
@ContextConfiguration(locations = "classpath:spring.xml") //指定xml配置类
/*@ContextConfiguration(classes = Application.class)*/
public class BaseTestCase {
}

