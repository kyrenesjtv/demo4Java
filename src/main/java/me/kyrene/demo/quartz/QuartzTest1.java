package me.kyrene.demo.quartz;

import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.util.Date;

/**
 * Created by wanglin on 2017/10/19.
 */
public class QuartzTest1 {

    @Test
    public  void test1(){
        try{
            //创建scheduel
            Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();

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

            defaultScheduler.scheduleJob(jobDetail,simpleTrigger);
            defaultScheduler.start();

            //让线程继续运行
            Thread.sleep(10000);
            defaultScheduler.shutdown();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public  void test2(){
        try{
            //创建scheduel
            Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();

            //定义一个Trigger
            CronTriggerImpl cronTrigger = new CronTriggerImpl();
            cronTrigger.setName("trigger1");
            cronTrigger.setStartTime(new Date(new Date().getTime() + 1000));
            cronTrigger.setCronExpression("0/10 * * * * ? ");//每隔1s出发

            //设置一个job
            JobDetailImpl jobDetail = new JobDetailImpl();
            jobDetail.setName("Job1");
            jobDetail.setJobClass(Myjob1.class);
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("1","hello word");
            jobDetail.setJobDataMap(jobDataMap);

            defaultScheduler.scheduleJob(jobDetail,cronTrigger);
            defaultScheduler.start();

            //让线程继续运行
            Thread.sleep(10000);
            defaultScheduler.shutdown();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /*
通配符说明:
* ：表示所有值. 例如:在分的字段上设置 “*”,表示每一分钟都会触发。
?： 表示不指定值。使用的场景为不需要关心当前设置这个字段的值。例如:要在每月的10号触发一个操作，但不关心是周几，所以需要周位置的那个字段设置为”?” 具体设置为 0 0 0 10 * ?
-： 表示区间。例如 在小时上设置 “10-12”,表示 10,11,12点都会触发。
, ：表示指定多个值，例如在周字段上设置 “MON,WED,FRI” 表示周一，周三和周五触发
/： 用于递增触发。如在秒上面设置”5/15” 表示从5秒开始，每增15秒触发(5,20,35,50)。 在月字段上设置’1/3’所示每月1号开始，每隔三天触发一次。
L： 表示最后的意思。在日字段设置上，表示当月的最后一天(依据当前月份，如果是二月还会依据是否是润年[leap]), 在周字段上表示星期六，相当于”7”或”SAT”。如果在”L”前加上数字，则表示该数据的最后一个。例如在周字段上设置”6L”这样的格式,则表示“本 月最后一个星期五”
W ： 表示离指定日期的最近那个工作日(周一至周五). 例如在日字段上设置”15W”，表示离每月15号最近的那个工作日触发。如果15号正好是周六，则找最近的周五(14号)触发, 如果15号是周未，则找最近的下周一(16号)触发.如果15号正好在工作日(周一至周五)，则就在该天触发。如果指定格式为 “1W”,它则表示每月1号往后最近的工作日触发。如果1号正是周六，则将在3号下周一触发。(注，”W”前只能设置具体的数字,不允许区间”-“).

小提示：
‘L’和 ‘W’可以一组合使用。如果在日字段上设置”LW”,则表示在本月的最后一个工作日触发(一般指发工资 )
序号(表示每月的第几个周几)：，例如在周字段上设置”6#3”表示在每月的第三个周六.注意如果指定”#5”,正好第五周没有周六，则不会触发该配置(用在母亲节和父亲节再合适不过了)

常用示例:
0 0 12 * * ? 每天12点触发
0 15 10 ? * * 每天10点15分触发
0 15 10 * * ? 每天10点15分触发
0 15 10 * * ? * 每天10点15分触发
0 15 10 * * ? 2005 2005年每天10点15分触发
0 * 14 * * ? 每天下午的 2点到2点59分每分触发
0 0/5 14 * * ? 每天下午的 2点到2点59分(整点开始，每隔5分触发)
0 0/5 14,18 * * ? 每天下午的 2点到2点59分(整点开始，每隔5分触发)，每天下午的 18点到18点59分(整点开始，每隔5分触发)
0 0-5 14 * * ? 每天下午的 2点到2点05分每分触发
0 10,44 14 ? 3 WED 3月分每周三下午的 2点10分和2点44分触发
0 15 10 ? * MON-FRI 从周一到周五每天上午的10点15分触发
0 15 10 15 * ? 每月15号上午10点15分触发
0 15 10 L * ? 每月最后一天的10点15分触发
0 15 10 ? * 6L 每月最后一周的星期五的10点15分触发
0 15 10 ? * 6L 2002-2005 从2002年到2005年每月最后一周的星期五的10点15分触发
0 15 10 ? * 6#3 每月的第三周的星期五开始触发
0 0 12 1/5 * ? 每月的第一个中午开始每隔5天触发一次
0 11 11 11 11 ? 每年的11月11号 11点11分触发(光棍节)
*/
}
