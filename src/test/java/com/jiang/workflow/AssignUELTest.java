package com.jiang.workflow;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动流程实例，动态设置 assignee
 *
 * @author Bryan.luo
 * @CreateBy 2020/12/12 星期六
 * @description
 **/
public class AssignUELTest extends WorkflowApplicationTests {


    @Test
    public void deployment(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("activiti/hasUel.bpmn")
                .addClasspathResource("activiti/hasUel.bpmn")
                .name("UEL定义的请假流程")
                .deploy();
        System.out.println("流程部署ID: " + deployment.getId());
        System.out.println("流程部署名称: " + deployment.getName());
    }

    /**
     *
     * 流程图设置审批人的时候，使用${param}来设置， 这里事项对占位符实现赋值
     *
     */
    @Test
    public void setAssign() {

        // 1、设置 assignee 的取值， 用户可以在界面上设置流程的执行人
        Map<String, Object> assignMap = new HashMap<>();
        assignMap.put("assignee01", "luo");
        assignMap.put("assignee02", "shi");
        assignMap.put("assignee03", "jiang");

        // 2、启动流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("leave", assignMap);

        // 输出
        System.out.println("流程定义ID: " + instance.getProcessDefinitionId());
        System.out.println("流程实例ID: " + instance.getId());
        System.out.println("当前活动的ID: " + instance.getActivityId());
    }

}
