package com.kaishengit.timer;

import java.util.TimerTask;

public class MyTimeTask extends TimerTask {


    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "hello,timerTask start");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello,timerTask end");

    }
}
