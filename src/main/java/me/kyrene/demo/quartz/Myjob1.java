package me.kyrene.demo.quartz;

import org.quartz.*;

import java.util.Map;

/**
 * Created by wanglin on 2017/10/19.
 */
public class Myjob1 implements Job {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        for(Map.Entry<String ,Object> map :jobDataMap.entrySet()){
            System.out.println("key:"+map.getKey()+"    value:"+map.getValue());
        }
        System.out.println("myjob is working");
        System.out.println("------------");
    }
}
