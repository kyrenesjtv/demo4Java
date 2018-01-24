package me.kyrene.demo.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * Created by wanglin on 2018/1/24.
 */
public class TaskListnerImpl implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        //指定个人任务的办理人，也可以指定组任务的办理人
        //这里查询数据库，将下一任务的办理人查询获取
        String name = "";
        delegateTask.setAssignee(name);
    }
}
