package com.jiang.workflow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

/**
 * 模拟请假流程
 *
 * @author shijiang.luo
 * @description
 * @date 2020-06-29 22:54
 */
public class Helloworld {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    @Test
    public void deployProcess() {

        // 得到流程部署的 service
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment().name("请假流程001")
                                .addClasspathResource("activiti/helloworld.bpmn")
                                .addClasspathResource("activiti/helloworld.png")
                                .deploy();
        System.out.println("部署成功：流程部署ID：" + deployment.getId() + ", " + deployment.toString());
    }

}