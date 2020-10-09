package com.jiang.workflow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipInputStream;

/**
 *
 * 管理流程定义
 *
 * @author shijiang.luo
 * @description
 * @date 2020-06-30 22:31
 */
public class ProcessDefTest {

    // 得到流程引擎
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void deployByZip(){
        InputStream inputStream = this.getClass().getResourceAsStream("/activiti/helloworld.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        Deployment deployment = repositoryService
                .createDeployment()
                .name("请假流程002")
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("部署成功：" + deployment.getId());
    }

    /**
     *
     *  查询
     *
     *
     */
    @Test
    public void queryDeployment(){
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        // 创建部署信息的查询
        repositoryService
                .createDeploymentQuery()
                // 条件
                //.deploymentId() 根据部署ID查询
                //.deploymentName() 根据部署名称查询
                //.deploymentTenantId() 根据tenantId 查询
                //.deploymentNameLike() 根据名称模糊查询

                // 排序
                //.orderByDeploymentId().asc() 根据部署id升序
                //.orderByDeploymenTime().desc() 根据部署事件降序

                // 结果集
                //.singleResult()
                //.count()

                //.listPage()
        .list();
    }

    /**
     *
     * 删除
     *
     */
    @Test
    public void deleteDeployment(){
        String deploymentId = "";
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        // 如果该流程定义已经启动，则删除失败，会抛出异常
        repositoryService.deleteDeployment(deploymentId);
        // 无论流程启动没有，都会将流程删除
        repositoryService.deleteDeployment(deploymentId, true);
    }


    /**
     *
     *
     * 流程定义的修改
     *
     */
    @Test
    public void viewProcessImg() throws IOException{
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        String processDefId = "helloworld:2:27504";
        InputStream inputStream = repositoryService.getProcessModel(processDefId);
        try(OutputStream out = new FileOutputStream("/Users/shijiang/Desktop/helloworld.png")){
            byte [] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1){
                out.write(bytes, 0 , len);
                out.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            inputStream.close();
        }

    }

}