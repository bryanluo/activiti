package com.jiang.workflow;

import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;

/**
 *
 *
 * 全部流程实例的挂起与激活：
 *
 * 操作流程定义为挂起专题，该流程定义下边所有的流程实例全部暂停。
 * 流程定义为挂起状态该流程定义将不允许启动新的流程实例，同时该流程定义下所有的流程实例将全部挂起暂停执行。
 *
 * @author Bryan.luo
 * @CreateBy 2020/12/12 星期六
 * @description
 **/
public class SuspendProcessInstanceTest extends WorkflowApplicationTests{

    @Test
    public void suspendAndActiveProcessInstance(){

        // 1. 查询流程定义的对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("helloworld").singleResult();
        // 2. 得到当前流程定义的实例是否都为暂停状态
        Boolean suspended = processDefinition.isSuspended();

        String processDefinitionId = processDefinition.getId();
        // 3、判断
        if(suspended){
            // 说明是暂停状态，就可以操作
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            System.out.println("流程定义：" + processDefinitionId + "， 激活");
        }else{
            repositoryService.suspendProcessDefinitionById(processDefinitionId);
            System.out.println("流程定义：" + processDefinitionId + "， 挂起");
        }
    }

}
