package com.jiang.workflow.mode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiang.workflow.WorkflowApplicationTests;
import com.jiang.workflow.common.exception.UserFriendlyException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Bryan.luo
 * @CreateBy 2021/1/3 星期日
 * @description
 *
 * 在线绘制流程以后这里从创建到执行流程的具体步骤
 **/
@Slf4j
public class ModeServiceTest extends WorkflowApplicationTests {

    @Test
    public void deploy() throws IOException {
        Model mode = repositoryService.getModel("1");
        byte [] bytes = repositoryService.getModelEditorSource(mode.getId());
        Optional.ofNullable(bytes).orElseThrow(() -> new UserFriendlyException("模型数据为空，请添加流程模型"));
        JsonNode modeNode = new ObjectMapper().readTree(bytes);
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modeNode);
        if(bpmnModel.getProcesses().size() == 0){
            throw new UserFriendlyException("数据模型不符要求，请至少设计一条主线流程");
        }
        byte [] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

        // 发布流程
        String processName = mode.getName().concat(".bpmn20.xml");
        Deployment deploy = repositoryService.createDeployment()
                .name(mode.getName())
                .addString(processName, new String(bpmnBytes, "utf-8"))
                .deploy();
        // model 绑定 deployment
        mode.setDeploymentId(deploy.getId());
        repositoryService.saveModel(mode);
        log.info("流程发布成功, deploymentId: {}, name: {}", deploy.getId(), deploy.getName());
    }


    @Test
    public void createProcessDefinitionByDeployment(){

        repositoryService.activateProcessDefinitionByKey("holiday");

    }


}
