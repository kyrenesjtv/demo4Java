package me.kyrene.demo.quartz;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * Quartz的各种操控
 * Created by wanglin on 2017/10/21.
 */
public class QuartzUtils {

    public JobDetailImpl newJob(String name , Map<String,Object> map ,Class<? extends Job> jobClass){
        //设置一个job
        JobDetailImpl jobDetail = new JobDetailImpl();
        if(name == null || "".equals(name)){
            throw  new IllegalArgumentException(" name cannot be null ");
        }else {
            jobDetail.setName(name);
        }
        JobDataMap jobDataMap = null;
        if(map == null){
            throw  new IllegalArgumentException(" map cannot be null ");
        }else{
            jobDataMap= new JobDataMap();
            for (Map.Entry<String ,Object> data : map.entrySet()){
                jobDataMap.put(data.getKey(),data.getValue());
            }
        }
        jobDetail.setJobDataMap(jobDataMap);
        if(jobClass == null) {
            throw new IllegalArgumentException("Job class cannot be null.");
        } else if(!Job.class.isAssignableFrom(jobClass)) {
            throw new IllegalArgumentException("Job class must implement the Job interface.");
        } else {
            jobDetail.setJobClass(jobClass);
        }
        return jobDetail;
    }

    public SimpleTriggerImpl newSimpleTrigger(String name,int repeatCount,long repeatInterval,Date startTime) throws SchedulerException {
        //定义一个Trigger
        SimpleTriggerImpl simpleTrigger = new SimpleTriggerImpl();
        if(name == null || "".equals(name)){
            throw  new IllegalArgumentException(" name cannot be null ");
        }else {
            simpleTrigger.setName(name);
        }

        if(repeatCount <= 0){
            throw  new IllegalArgumentException(" repeatCount cannot be < 0 ");
        }else {
            simpleTrigger.setRepeatCount(repeatCount);
        }

        if(repeatInterval <= 0){
            throw  new IllegalArgumentException(" repeatInterval cannot be < 0 ");
        }else {
            simpleTrigger.setRepeatInterval(repeatInterval);
        }
        if(startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        } else {
            long eTime =  new Date().getTime();
            if(startTime.getTime() > eTime ) {
                throw new IllegalArgumentException(" startTime must > currentTime");
            } else {
                simpleTrigger.setStartTime(startTime);
            }
        }
        return simpleTrigger ;
    }

    public CronTrigger newcronTrigger(String name ,Date startTime ,String cronExpression) throws ParseException {
        //定义一个Trigger
        CronTriggerImpl cronTrigger = new CronTriggerImpl();
        if(name == null || "".equals(name)){
            throw  new IllegalArgumentException(" name cannot be null ");
        }else {
            cronTrigger.setName(name);
        }
        if(startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        } else {
            long eTime =  new Date().getTime();
            if(startTime.getTime() > eTime ) {
                throw new IllegalArgumentException(" startTime must > currentTime");
            } else {
                cronTrigger.setStartTime(startTime);
            }
        }
        if(cronExpression == null || "".equals(cronExpression)){
            throw  new IllegalArgumentException(" cronExpression cannot be null ");
        }else {
            cronTrigger.setCronExpression(cronExpression);
        }
        return cronTrigger;
    }


    public Scheduler getDefaultScheduler() throws SchedulerException {
        return StdSchedulerFactory.getDefaultScheduler();
    }

    public void setScheduleJob(Scheduler scheduler,JobDetail var1, Trigger var2) throws SchedulerException {
        scheduler.scheduleJob(var1,var2);
    }

    public void start(Scheduler scheduler) throws SchedulerException {
        scheduler.start();
    }

    public void shutDown(Scheduler scheduler) throws SchedulerException {
        scheduler.shutdown();
    }

}
