package me.kyrene.demo.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
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
        //但一般都是在前端通过下拉框获取人来设置办理人
        ProcessInstance instance = runtimeService.startProcessInstanceById(name);//这里也可以放值，用来指定下一办理人(或者是多个)
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
     * 将当前任务给其他人 ,应该是在执行任务的时候调用这个方法。
     */
    @Test
    public void changeTaskToAnother() {
        String id= "";
        String userID="";
        taskService.setAssignee(id,userID);
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
     * 查询个人历史任务
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

    /**
     * 获取该流程的所有任务
     */
    @Test
    public void getAllHistoryProcessID() {
        //流程实例ID
        String id = "";
        List<HistoricIdentityLink> list = historyService.getHistoricIdentityLinksForProcessInstance(id);
        if(list != null && list.size()>0){
            for(HistoricIdentityLink iden :list){
                System.out.println(iden.getUserId());//userid
                System.out.println(iden.getProcessInstanceId());//流程实例ID
            }

        }
    }

    /**
     * 查询个人任务
     */
    @Test
    public void getCurrentTaskByPerson() {
        String name="";
        List<Task> list = taskService.createTaskQuery().taskAssignee(name).list();
        if(list != null &&list.size()>0){
            for(Task task :list){
                System.out.println(task.getAssignee());//当前人
                System.out.println(task.getId());//任务ID
                System.out.println(task.getProcessInstanceId());//流程实例ID
            }
        }
    }
    /**
     * 查询组任务
     */
    @Test
    public void getCurrentTaskByPersons() {
        String name="";
        //Candidate 候选人 participant 参与者
        List<Task> list = taskService.createTaskQuery().taskCandidateUser(name).list();
        if(list != null &&list.size()>0){
            for(Task task :list){
                System.out.println(task.getAssignee());//当前人
                System.out.println(task.getId());//任务ID
                System.out.println(task.getProcessInstanceId());//流程实例ID
            }
        }
    }
    /**
     * 查询正在执行的任务的办理人
     */
    @Test
    public void getCurrentPersonsByTaskID() {
        String id ="";//任务ID
        List<IdentityLink> persons = taskService.getIdentityLinksForTask(id);
        if(persons!=null && persons.size()>0){
            for(IdentityLink identity : persons){
                System.out.println(identity.getUserId());//userid
                System.out.println(identity.getTaskId());//任务id
                System.out.println(identity.getProcessInstanceId());//流程实例ID
                System.out.println(identity.getType());//用户的类型
            }
        }
    }

    /**
     * 拾取任务，将组任务给个人任务
     */
    @Test
    public void groupTaskToPersonTask() {
        String taskID="";
        String userID="";
        taskService.claim(taskID,userID);//这里肯定是循环添加，userID可以获取，也可以前端指定
    }

    /**
     * 将个人任务回退到组任务
     */
    @Test
    public void personTaskToGroupTask() {
        String taskID="";
        taskService.claim(taskID,null);
    }

    /**
     * 退回到上一节点
     */
    @Test
    public void rollbackPreviousTask() {
        //当前节点ID
        String taskId="";
        try {
            Map<String, Object> variables;
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService
                    .createHistoricTaskInstanceQuery().taskId(taskId)
                    .singleResult();
            // 取得流程实例，流程实例
            ProcessInstance instance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(currTask.getProcessInstanceId())
                    .singleResult();
            if (instance == null) {
//                log.info("流程结束");
//                log.error("出错啦！流程已经结束");
                System.out.println("流程出错或已经结束");
            }
            variables = instance.getProcessVariables();
            // 取得流程定义
            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryService) repositoryService)
                    .getDeploymentResourceNames(currTask.getProcessDefinitionId());
//                    .getDeployedProcessDefinition(currTask
//                            .getProcessDefinitionId());
            if (definition == null) {
//                log.info("流程定义未找到");
//                log.error("出错啦！流程定义未找到");
//                return "ERROR";
                System.out.println("流程未找到");
            }
            // 取得上一步活动
            ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
                    .findActivity(currTask.getTaskDefinitionKey());

            //也就是节点间的连线
            List<PvmTransition> nextTransitionList = currActivity
                    .getIncomingTransitions();
            // 清除当前活动的出口
            List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
            //新建一个节点连线关系集合

            List<PvmTransition> pvmTransitionList = currActivity
                    .getOutgoingTransitions();
            //
            for (PvmTransition pvmTransition : pvmTransitionList) {
                oriPvmTransitionList.add(pvmTransition);
            }
            pvmTransitionList.clear();

            // 建立新出口
            List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
            for (PvmTransition nextTransition : nextTransitionList) {
                PvmActivity nextActivity = nextTransition.getSource();
                ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition)
                        .findActivity(nextActivity.getId());
                TransitionImpl newTransition = currActivity
                        .createOutgoingTransition();
                newTransition.setDestination(nextActivityImpl);
                newTransitions.add(newTransition);
            }
            // 完成任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(instance.getId())
                    .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                taskService.complete(task.getId(), variables);
                historyService.deleteHistoricTaskInstance(task.getId());
            }
            // 恢复方向
            for (TransitionImpl transitionImpl : newTransitions) {
                currActivity.getOutgoingTransitions().remove(transitionImpl);
            }
            for (PvmTransition pvmTransition : oriPvmTransitionList) {
                pvmTransitionList.add(pvmTransition);
            }
//            log.info("OK");
//            log.info("流程结束");
            System.out.println("流程结束");

//            return "SUCCESS";
        } catch (Exception e) {
//            log.error("失败");
//            log.error(e.getMessage());
//            return "ERROR";
            System.out.println("退回失败");

        }
    }

    /**
     * 退回到指定节点
     */
    @Test
    public void rollbackSelectTask() {
        String currenttaskID="";
        String selecttaskID="";
//        try {
            Map<String, Object> variables;
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService
                    .createHistoricTaskInstanceQuery().taskId(currenttaskID)
                    .singleResult();
            // 取得流程实例，流程实例
            ProcessInstance instance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(currTask.getProcessInstanceId())
                    .singleResult();
            if (instance == null) {
//                log.info("流程结束");
//                log.error("出错啦！流程已经结束");
//                return "ERROR";
                System.out.println("流程结束");
            }
            variables = instance.getProcessVariables();
            // 取得流程定义
            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryService) repositoryService)
                    .getProcessDefinition(currTask
                            .getProcessDefinitionId());
            if (definition == null) {
//                log.info("流程定义未找到");
//                log.error("出错啦！流程定义未找到");
//                return "ERROR";
                System.out.println("流程定义未找到");
            }
            //取得当前活动节点
            ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
                    .findActivity(currTask.getTaskDefinitionKey());

//            log.info("currActivity"+currActivity);
            // 取得上一步活动
            //也就是节点间的连线
            //获取来源节点的关系
            List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();

            // 清除当前活动的出口
            List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
            //新建一个节点连线关系集合
            //获取出口节点的关系
            List<PvmTransition> pvmTransitionList = currActivity
                    .getOutgoingTransitions();
            //
            for (PvmTransition pvmTransition : pvmTransitionList) {
                oriPvmTransitionList.add(pvmTransition);
            }
            pvmTransitionList.clear();

            // 建立新出口
            List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
            for (PvmTransition nextTransition : nextTransitionList) {
                PvmActivity nextActivity = nextTransition.getSource();

//                log.info("nextActivity"+nextActivity);
//
//                log.info("nextActivity.getId()"+nextActivity.getId());

                //destTaskkey
                ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition)
                        // .findActivity(nextActivity.getId());这个是连接下一个
                        .findActivity(selecttaskID);
                TransitionImpl newTransition = currActivity
                        .createOutgoingTransition();
                newTransition.setDestination(nextActivityImpl);
                newTransitions.add(newTransition);
            }
            // 完成任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(instance.getId())
                    .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                taskService.complete(task.getId(), variables);
                historyService.deleteHistoricTaskInstance(task.getId());
            }
            // 恢复方向
            for (TransitionImpl transitionImpl : newTransitions) {
                currActivity.getOutgoingTransitions().remove(transitionImpl);
            }
            for (PvmTransition pvmTransition : oriPvmTransitionList) {

                pvmTransitionList.add(pvmTransition);
            }
//            log.info("OK");
//            log.info("流程结束");
            System.out.println("流程结束");

//            return "SUCCESS";
//        } catch (Exception e) {
//            log.error("失败");
//            log.error(e.getMessage());
//            return "ERROR";
//        }
    }
}
