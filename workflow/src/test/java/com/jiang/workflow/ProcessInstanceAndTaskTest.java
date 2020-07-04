package com.jiang.workflow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 *
 * 流程实例和任务
 *
 * @author shijiang.luo
 * @description
 * @date 2020-07-01 20:24
 */
public class ProcessInstanceAndTaskTest {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void start() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        /**
         *
         *  processDefId:String 流程实例ID
         *
         *  businessId:String 业务ID 把业务ID和流程实例ID进行绑定
         *
         */
        // runtimeService.startProcessInstanceById(processDefId, businessId);
        /**
         *
         *  processDefId:String 流程实例ID
         *
         *  businessId:String 业务ID 把业务ID和流程实例ID进行绑定
         *
         *  variables:Map<String, Object> 流程变量， 决定流程的走向
         */
        //runtimeService.startProcessInstanceById(processDefinitionId, businessKey, variables);

        // 实际开发中我们使用的是：
        String processInstanceKEy = "helloworld";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceKEy);
        System.out.println("启动成功：" + processInstance.getId() + ", " + processInstance.getName());
    }


    /**
     *
     *   查询个人任务
     *
     */
    @Test
    public void queryTask(){
        TaskService taskService = this.processEngine.getTaskService();
        String assignee = "张三";
        List<Task> taskList = taskService.createTaskQuery()
        // 条件
        .taskAssignee(assignee)
        //.executionId(executionId)
        //.processInstanceId(processInstanceId)
        //.processDefinitionKey(processDefKey)
        //.processDefinitionId(processDefId)

        // 排序
        .orderByProcessDefinitionId().asc()
        // 结果集
        .list();


        taskList.forEach(task -> {
            System.out.println("任务ID：" + task.getId());
            System.out.println("任务名称：" + task.getName());
            System.out.println("任务实例ID：" + task.getProcessInstanceId());
            System.out.println("流程定义ID：" + task.getProcessDefinitionId());
            System.out.println("变量：" + task.getProcessVariables());
            System.out.println("创建事件：" + task.getCreateTime());
            System.out.println("任务持续时间：" + task.getDueDate());
            System.out.println("=======================================");
        });
    }


    /**
     *
     *  任务完成
     *
     */
    @Test
    public void completeTask(){
        TaskService taskService = processEngine.getTaskService();
        String taskId = "30005";
        // 根据任务ID去完成任务
        taskService.complete(taskId);
        // 任务完成并指定任务变量
        //taskService.complete(taskId, Map<String, Object> variables)
        System.out.println("完成任务:" + taskId);
    }

    /**
     *
     * 判断流程是否结束
     * 目的：
     *  更新业务表的状态
     */
    @Test
    public void isCompleteTask(){
        RuntimeService runtimeService =this.processEngine.getRuntimeService();
        // 已知流程实例ID
        String processInstanceid = "";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceid)
                .singleResult();
        if(processInstance == null){
            System.out.println("流程已经结束");
        }else{
            System.out.println("流程没有结束");
        }
        // 已知任务ID
        // 根据任务ID查询任务实例
        // 从任务实例里面去除流程实例
        String taskId = "";
        TaskService taskService  = this.processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .taskId(taskId).singleResult();
        processInstanceid = task.getProcessInstanceId();
        processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceid)
                .singleResult();
        if(processInstance == null){
            System.out.println("流程已经结束");
        }else{
            System.out.println("流程没有结束");
        }
    }

}