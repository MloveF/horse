package com.kaishengit.timer;

import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;

public class MyTimeTaskTest {


    @Test
    public void test() throws IOException {
        MyTimeTask myTimeTask = new MyTimeTask();

        Timer timer = new Timer();
        //将任务延迟三秒执行
        //timer.schedule(myTimeTask,3000);

        //在指定的时间去执行任务
        //timer.schedule(myTimeTask,new Date());

        //延迟0毫秒，并每秒钟执行一次  delay延迟  period期
        //timer.schedule(myTimeTask,0,1000);
        //从指定时间开始执行 每隔两秒执行一次
        timer.schedule(myTimeTask,new Date(),2000);

        System.in.read();

    }

}
