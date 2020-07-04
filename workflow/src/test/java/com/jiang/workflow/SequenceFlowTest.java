package com.jiang.workflow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 *
 * 连线
 *
 * @author shijiang.luo
 * @description
 * @date 2020-07-04 11:21
 */
public class SequenceFlowTest {


    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     *
     *  流程部署
     *
     */
    @Test
    public void deploy(){
        RepositoryService repositoryService = this.processEngine.getRepositoryService();

        repositoryService.deleteDeployment("42501");

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("activiti/SequenceFlowBPMN.bpmn")
                .addClasspathResource("activiti/SequenceFlowBPMN.png")
                .name("连线分支测试")
                .deploy();
        System.out.println("部署流程成功：" + deployment.getId());

    }


    /**
     *
     *  启动流程
     *
     */
    @Test
    public void start(){
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        // 先根据部署ID获取流程定义ID
        String deploymentId = "45001";
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();
        if(null != processDefinition) {
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
            System.out.println("流程启动成功：" + processInstance.getId());
            System.out.println("流程启动成功：" + processInstance.getName());
        }
    }


    @Test
    public void query(){
        TaskService taskService = this.processEngine.getTaskService();

        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("李四")
                .list();
        System.out.println(taskList.toString());
    }


    /**
     *  开始提交申请
     */
    @Test
    public void complete(){
        String taskId = "52503";
        TaskService taskService = this.processEngine.getTaskService();
        // ${outcome == '重要'}
        //taskService.complete(taskId, Maps.newHashMap("mean", "同意"));
        taskService.complete("55004");
        taskService.complete("55007");
        System.out.println("任务完成:" + taskId);
    }


}