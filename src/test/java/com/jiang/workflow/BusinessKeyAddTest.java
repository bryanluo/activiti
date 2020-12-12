package com.jiang.workflow;

import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

/**
 * @author Bryan.luo
 * @CreateBy 2020/12/12 星期六
 * @description
 **/
public class BusinessKeyAddTest extends WorkflowApplicationTests{

    /**
     *
     * 启动流程实例，添加进businessKey
     *
     * 本质：act_ru_execution表中的businessKey字段要存入业务标识
     *
     */
    @Test
    public void businessKeyAdd(){
        // 第一个参数是流程定义KEY
        // 第二个参数是业务标识businessKey
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("helloworld", "这里写入请假单的ID");
        System.out.println(instance.getBusinessKey());
    }

}
