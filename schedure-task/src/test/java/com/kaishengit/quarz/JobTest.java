package com.kaishengit.quarz;

import com.kaishengit.quartz.MyQuartzJob;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

public class JobTest {


    @Test
    public void simpleTrigger() throws SchedulerException, IOException {
        //1.定义任务  JobDetail工作的细节  JobBuilder工作的建设者
        JobDetail jobDetail = JobBuilder.newJob(MyQuartzJob.class).build();
        //2.定义触发的形式
        SimpleScheduleBuilder ssb = SimpleScheduleBuilder.simpleSchedule();
        //在秒间隔
        ssb.withIntervalInSeconds(3);
        //重复直到永远
        ssb.repeatForever();
        //3.通过触发形式创建触发器
        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(ssb).build();
        //4.创建任务调度者对象
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        //5.调度任务
        scheduler.scheduleJob(jobDetail,trigger);
        scheduler.start();

        System.in.read();

    }

    @Test
    public void cronTrigger() throws SchedulerException, IOException {
        //定义任务
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("phoneNumber","18236508993");

        JobDetail jobDetail = JobBuilder.newJob(MyQuartzJob.class)
                                        .setJobData(jobDataMap)
                                        .build();
        //2.定义触发的形式
        ScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/3 * * * * ? ");
        //3. 通过触发形式创建触发器
        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(scheduleBuilder).build();
        //4.创建任务调度者对象
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.scheduleJob(jobDetail,trigger);
        scheduler.start();

        System.in.read();


    }

}
