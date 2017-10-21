package me.kyrene.demo.quartz;

import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.util.Date;

/**
 * Created by wanglin on 2017/10/21.
 */
public class QuartzScheduleMgrTest {
    @Test
    public void test1() throws SchedulerException {
        QuartzScheduleMgr quartzScheduleMgr = new QuartzScheduleMgr();
        //定义一个Trigger
        SimpleTriggerImpl simpleTrigger = new SimpleTriggerImpl();
        simpleTrigger.setName("Trigger1");
        //重复次数
        simpleTrigger.setRepeatCount(10);
        //执行间隔
        simpleTrigger.setRepeatInterval(1000);
        //多久后后执行
        simpleTrigger.setStartTime(new Date(new Date().getTime()+1000));
        //设置优先级
        //            simpleTrigger.setPriority(10);

        //设置一个job
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setName("Job1");
        jobDetail.setJobClass(Myjob1.class);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("1","hello word");
        jobDetail.setJobDataMap(jobDataMap);

        quartzScheduleMgr.scheduleJob(jobDetail,simpleTrigger);
        quartzScheduleMgr.start();
        System.out.println("is satrt:"+quartzScheduleMgr.isStart());
        quartzScheduleMgr.shutDown();
        System.out.println("is satrt:"+quartzScheduleMgr.isShutDown());
    }
}
