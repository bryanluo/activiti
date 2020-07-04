package com.jiang.workflow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.assertj.core.internal.Maps;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 *
 * 流程变量
 *
 * 相关表：
 * ACT_RU_VARIABLE、ACT_HI_VARINST
 *
 * @author shijiang.luo
 * @description
 * @date 2020-07-01 23:28
 */
public class ProcessVariableTest {


    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void deployment(){
        // 获取资源管理服务
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        // 获取Zip文件
        InputStream inputStream = this.getClass().getResourceAsStream("/activiti/helloworld.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment()
                .name("设置流程变量")
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("部署成功：" + deployment.getId()+"," + deployment.getName());
    }

    /**
     *
     * 启动流程实例 并 设置流程变量
     *
     */
    @Test
    public void start(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String processInstanceKey = "helloworld";
        Map<String, Object> variavleMap = new HashMap<String, Object>();
        variavleMap.put("test", "test");
        variavleMap.put("name", "张三");
        variavleMap.put("days", 5);
        variavleMap.put("reason", "约会");
        variavleMap.put("times", new Date());
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(processInstanceKey, variavleMap);
        System.out.println("启动流程成功："+ processInstance.getId());
        System.out.println("启动流程成功："+ processInstance.getName());
        System.out.println("启动流程成功："+ processInstance.getProcessVariables());
    }

    /**
     *
     * 设置流程变量
     *
     */
    @Test
    public void setProcessVariable(){
        // 第一种设置变量方式
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        String executionId = "37507";
        runtimeService.setVariable(executionId, "test", "看下可以更新不？");
        runtimeService.setVariable(executionId, "name", "jiang");
        System.out.println("流程变量设置成功！");
        // 第二种设置变量方式
        TaskService taskService = this.processEngine.getTaskService();
        String taskId = "";
        taskService.setVariable(taskId, "task", "任务ID设置变量");

    }

    /**
     *
     * 获取流程变量
     *
     */
    @Test
    public void getProcessVariable(){
       String executionId = "37507";
       RuntimeService runtimeService = processEngine.getRuntimeService();
       Object val = runtimeService.getVariable(executionId, "test");
        System.out.println("获取到的流程变量：" + val);
    }

}