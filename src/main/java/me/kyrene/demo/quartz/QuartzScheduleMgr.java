package me.kyrene.demo.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wanglin on 2017/10/21.
 */
public class QuartzScheduleMgr {

    private Scheduler scheduler = getScheduler();

    private static Scheduler getScheduler() {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = null;
        if (scheduler == null) {
            try {
                scheduler = stdSchedulerFactory.getScheduler();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        return scheduler;
    }

    /**
     * 初始化scheduler对象
     *
     * @return 初始化后的scheduler对象
     */
    public Scheduler instance() {
        return scheduler;
    }

    /**
     * 启动scheduler对象
     *
     * @throws SchedulerException
     */
    public void start() throws SchedulerException {
        scheduler.start();
    }

    /**
     * 检查是否启动
     *
     * @throws SchedulerException
     */
    public boolean  isStart() throws SchedulerException {
        return scheduler.isStarted();
    }

    /**
     * 关闭scheduler对象
     *
     * @throws SchedulerException
     */
    public void shutDown() throws SchedulerException {
        scheduler.shutdown();
    }

    /**
     * 检查是否关闭
     *
     * @throws SchedulerException
     */
    public boolean isShutDown() throws SchedulerException {
        return scheduler.isShutdown();
    }

    /**
     * 添加多规格job
     * @param triggersAndJobs
     * @param replace
     * @throws SchedulerException
     */
    public  void scheduleJobs(Map<JobDetail, Set<? extends Trigger>> triggersAndJobs, boolean replace) throws SchedulerException {
        scheduler.scheduleJobs(triggersAndJobs,replace);
    }

    /**
     * 添加job
     *
     * @param var1
     * @param var2
     * @return
     * @throws SchedulerException
     */
    public Date scheduleJob(JobDetail var1, Trigger var2) throws SchedulerException {
        return scheduler.scheduleJob(var1, var2);
    }

    /**
     * 添加job
     *
     * @param var1
     * @return
     * @throws SchedulerException
     */
    public Date scheduleJob(Trigger var1) throws SchedulerException {
        return scheduler.scheduleJob(var1);
    }

    /**
     * 取消某个Job
     * @param var1
     * @return
     * @throws SchedulerException
     */
    public boolean unscheduleJob(TriggerKey var1) throws SchedulerException {
        return scheduler.unscheduleJob(var1);
    }

    /**
     * 取消多个job
     * @param var1
     * @return
     * @throws SchedulerException
     */
    public boolean unscheduleJobs(List<TriggerKey> var1) throws SchedulerException {
        return scheduler.unscheduleJobs(var1);
    }

    /**
     * 重新制定某个Job
     * @param var1
     * @param var2
     * @return
     * @throws SchedulerException
     */
    public Date rescheduleJob(TriggerKey var1, Trigger var2) throws SchedulerException {
        return scheduler.rescheduleJob(var1,var2);
    }

    /**
     * 添加相关的job
     * @param var1
     * @param var2
     * @throws SchedulerException
     */
    public  void addJob(JobDetail var1, boolean var2) throws SchedulerException {
        scheduler.addJob(var1,var2);
    }

    /**
     * 添加相关的job
     * @param var1
     * @param var2
     * @param var3
     * @throws SchedulerException
     */
    public void addJob(JobDetail var1, boolean var2, boolean var3) throws SchedulerException {
        scheduler.addJob(var1,var2,var3);
    }

    /**
     * 删除某个Job
     * @param var1
     * @return
     * @throws SchedulerException
     */
    public boolean deleteJob(JobKey var1) throws SchedulerException {
        return scheduler.deleteJob(var1);
    }

    /**
     * 删除多个job
     * @param var1
     * @return
     * @throws SchedulerException
     */
    public  boolean deleteJobs(List<JobKey> var1) throws SchedulerException {
        return scheduler.deleteJobs(var1);
    }

    /**
     *
     * @param var1
     * @throws SchedulerException
     */
    public void triggerJob(JobKey var1) throws SchedulerException {
        scheduler.triggerJob(var1);
    }

    /**
     *
     * @param var1
     * @param var2
     * @throws SchedulerException
     */
   public  void triggerJob(JobKey var1, JobDataMap var2) throws SchedulerException {
       scheduler.triggerJob(var1,var2);
   }

    /**
     * 暂停某个Job
     * @param var1
     * @throws SchedulerException
     */
   public void pauseJob(JobKey var1) throws SchedulerException {
       scheduler.pauseJob(var1);
   }

    /**
     * 暂停多个Job
     * @param var1
     */
   public void pauseJobs(GroupMatcher<JobKey> var1) throws SchedulerException {
       scheduler.pauseJobs(var1);
   }

    /**
     * 暂停所有
     * @throws SchedulerException
     */
   public void pauseAll() throws SchedulerException {
       scheduler.pauseAll();
   }

    /**
     * 恢复某个job
     * @param var1
     * @throws SchedulerException
     */
   public void resumeJob(JobKey var1) throws SchedulerException {
       scheduler.resumeJob(var1);
   }

    /**
     * 恢复多个job
     * @param var1
     */
   public void resumeJobs(GroupMatcher<JobKey> var1) throws SchedulerException {
       scheduler.resumeJobs(var1);
   }

    /**
     * 恢复所有J
     */
    public void resumeAll() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 暂停某个trigger
     * @param var1
     * @throws SchedulerException
     */
    public void pauseTrigger(TriggerKey var1) throws SchedulerException {
        scheduler.pauseTrigger(var1);
    }

    /**
     * 暂停多个trigger
     * @param var1
     * @throws SchedulerException
     */
    public void pauseTriggers(GroupMatcher<TriggerKey> var1) throws SchedulerException {
        scheduler.pauseTriggers(var1);
    }

    /**
     * 恢复某个triggger
     * @param var1
     */
    public void resumeTrigger(TriggerKey var1) throws SchedulerException {
        scheduler.resumeTrigger(var1);
    }

    /**
     * 恢复多个trigger
     * @param var1
     * @throws SchedulerException
     */
    public void resumeTriggers(GroupMatcher<TriggerKey> var1) throws SchedulerException {
        scheduler.resumeTriggers(var1);
    }
}
