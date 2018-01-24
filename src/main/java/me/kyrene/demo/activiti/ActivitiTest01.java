package me.kyrene.demo.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglin on 2018/1/24.
 */
public class ActivitiTest01 {


    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;


    @Before
    public void instanceXml() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-activiti.xml");
        repositoryService = (RepositoryService) context.getBean("repositoryService");
        runtimeService = (RuntimeService) context.getBean("runtimeService");
        taskService = (TaskService) context.getBean("taskService");
        historyService = (HistoryService) context.getBean("historyService");
    }

    /**
     * 部署文件并发布
     */
    @Test
    public void getInstance() {
        String filePath = "classpath:helloworld.bpmn";
        Deployment deploy = repositoryService.createDeployment().addClasspathResource(filePath).deploy();
        System.out.println(deploy.getName());//流程名称
        System.out.println(deploy.getId());//流程id
    }

    /**
     * 启动流程
     */
    @Test
    public void start() {
        String name = "helloword";
        ProcessInstance instance = runtimeService.startProcessInstanceById(name);
        System.out.println(instance.getId());//流程实例ID
    }

    /**
     * 当前任务
     */
    @Test
    public void findTaskByCurrentName() {
        String name = "张三";
        List<Task> list = taskService.createTaskQuery().taskAssignee(name).list();
        if (list!=null && list.size()>0) {
            for (Task task : list) {
                System.out.println(task.getName());//任务名称
                System.out.println(task.getDescription());//描述
                System.out.println(task.getId());//任务id
                System.out.println(task.getProcessInstanceId());//流程实例ID
            }
        }
    }

    /**
     * 提交任务
     */
    @Test
    public void comlpeteTask() {
        String id ="";
        //排他网关 if else
        Map<String, Object> map = new HashMap<>();
        map.put("key","1");//1 ，2 代表流程指向哪一个
        if(map == null){//平行网关跟单线一样，但是要都执行完之后任务才会完成
            taskService.complete(id);
        }
        taskService.complete(id,map);
    }

    /**
     * 删除流程
     */
    @Test
    public void deleteProcess() {
        String id = "";
        repositoryService.deleteDeployment(id,true);
    }

    /**
     * 查询个人流程
     */
    @Test
    public void getHistoryTask() {
        String name ="";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee(name).list();
        if(list !=null && list.size()>0){
            for(HistoricTaskInstance task :list){
                System.out.println(task.getProcessInstanceId());//流程实例ID
                System.out.println(task.getAssignee());//执行对象
                System.out.println(task.getStartTime());//开始时间
                System.out.println(task.getEndTime());//结束时间
            }

        }
    }
}
