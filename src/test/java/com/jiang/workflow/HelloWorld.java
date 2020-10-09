package com.jiang.workflow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 模拟请假流程
 *
 * @author shijiang.luo
 * @description
 * @date 2020-06-29 22:54
 */
public class HelloWorld {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void deployment() {
        // 得到流程部署的 service
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment().name("请假流程001")
                                .addClasspathResource("activiti/helloworld.bpmn")
                                .addClasspathResource("activiti/helloworld.png")
                                .deploy();
        System.out.println("部署成功：流程部署ID：" + deployment.getId() + ", " + deployment.toString());
    }

    /**
     *
     *  启动流程实例
     *
     */
    @Test
    public void start(){
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        String processDefinitionKey = "helloworld";
        ProcessInstance processInstance2  = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程启动成功：" + processInstance2.getId());
    }

    /**
     *
     *  查询任务
     *
     */
    @Test
    public void findTask(){
        TaskService taskService = this.processEngine.getTaskService();
        // 张三 -> 李四 -> 王五
        String assignee = "王五";
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned(assignee)
                .list();
        tasks.forEach(task -> {
            System.out.println("任务ID：" + task.getId());
            System.out.println("流程实例ID：" + task.getProcessDefinitionId());
            System.out.println("执行实例ID：" + task.getExecutionId());
            System.out.println("流程定义ID：" + task.getProcessDefinitionId());
            System.out.println("任务名称：" + task.getName());
            System.out.println("任务办理人：" + task.getAssignee());
            System.out.println("#################################");
        });

    }

    /**
     *
     *  办理任务
     *
     */
    @Test
    public void completeTask(){
        TaskService taskService = this.processEngine.getTaskService();
        String taskId = "22502";
        taskService.complete(taskId);
        System.out.println("任务完成: " + taskId);
    }

}