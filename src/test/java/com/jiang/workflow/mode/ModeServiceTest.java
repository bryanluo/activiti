package com.jiang.workflow.mode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiang.workflow.WorkflowApplicationTests;
import com.jiang.workflow.common.exception.UserFriendlyException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Bryan.luo
 * @Date 2021/1/3 星期日
 * @description
 *
 * 在线绘制流程以后这里从创建到执行流程的具体步骤
 **/
@Slf4j
public class ModeServiceTest extends WorkflowApplicationTests {

    private static final String FORMAT = "UTF-8";

    @Resource
    private ProcessEngineConfiguration processEngineConfiguration;

    /**
     * 流程部署并发布
     */
    @Test
    public void deploy() throws IOException {
        String modeId = "2503";
        Model mode = repositoryService.getModel(modeId);
        byte [] bytes = repositoryService.getModelEditorSource(mode.getId());
        Optional.ofNullable(bytes).orElseThrow(() -> new UserFriendlyException("模型数据为空，请添加流程模型"));
        // 将 byte[] 转换为 xml
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
                .addString(processName, new String(bpmnBytes, FORMAT))
                .deploy();
        // model 绑定 deployment
        mode.setDeploymentId(deploy.getId());
        repositoryService.saveModel(mode);
        log.info("流程发布成功, deploymentId: {}, name: {}", deploy.getId(), deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        String key = "holiday";
        // 这里设置全局变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("employee", "zhangsan");
        variables.put("personnel", "lisi");
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(key, variables);
        log.info("流程实例启动成功, id: {}, name: {}", instance.getId(), instance.getName());
    }

    /**
     * 完成当前任务
     */
    @Test
    public void complete(){
        String assignee = "wangwu";
        Task task = taskService.createTaskQuery().taskAssignee(assignee).singleResult();
        Optional.ofNullable(task).orElseThrow(() -> new UserFriendlyException("当前任务不存在"));
        // 这里设置全局变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("peronnel", "wangwu");
        taskService.complete(task.getId(), variables);
        log.info("当前任务完成, 任务ID: {}, 任务名称: {} , 处理人: {}", task.getId(), task.getName(), task.getAssignee());
    }

    /**
     * 查询模型列表
     */
    @Test
    public void list(){
        List<Model> modelList = repositoryService.createModelQuery()
                .listPage(0, 10);
        modelList.forEach(model -> log.info("id: {}, name:{}", model.getId(), model.getName()));
    }

    /**
     * 生成流程图
     */
    @Test
    public void queryProcessDiagram() throws IOException{
        // 1、获取历史流程实例
        String processInstanceId = "7501";
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        Optional.ofNullable(historicProcessInstance).orElseThrow(() -> new UserFriendlyException("流程实例不存在"));
        // 2、根据流程定义ID，获取流程输入流
        String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        InputStream processDiagramIs = repositoryService.getProcessDiagram(processDefinitionId);
        Optional.ofNullable(processDiagramIs).orElseThrow(() -> new UserFriendlyException("流程定义图不存在"));
        // 3、绘制图
        BufferedImage bufferedImage = ImageIO.read(processDiagramIs);
        File file = new File("workflow.png");
        if(!file.exists()){
            file.createNewFile();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            ImageIO.write(bufferedImage, "png", fileOutputStream);
            processDiagramIs.close();
            log.info("图片生成成功: {}", file.getAbsolutePath());
        }
    }

    /**
     * 流程图高亮显示
     */
    @Test
    public void heightLightDiagram() throws IOException{
        String processInstanceId = "7501";

        // 获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        Optional.ofNullable(historicProcessInstance).orElseThrow(()->
                new UserFriendlyException("流程实例不存在, processInstanceId=" + processInstanceId));

        // 获取流程中已经执行的节点，按照执行先后顺序排序
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceId()
                .asc()
                .list();

        // 高亮已经执行流程节点ID集合
        List<String> highLightedActiveIds = new ArrayList<>();
        historicActivityInstanceList.forEach(historicActivityInstance -> highLightedActiveIds.add(historicActivityInstance.getActivityId()));

        // 已经完成的流程实例列表
        List<HistoricProcessInstance> historicProcessInstanceFinishedList = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list();

        // 如果还没完成，流程图高亮颜色为绿色，如果已经完成为红色
        ProcessDiagramGenerator processDiagramGenerator = null;
        if(CollectionUtils.isEmpty(historicProcessInstanceFinishedList)){
            processDiagramGenerator = new DefaultProcessDiagramGenerator();
        }else{
            processDiagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        // 高亮流程已发生流转的线id集合
        List<String> highLightedFlowIds = getHighLightedFlows(bpmnModel, historicActivityInstanceList);
        // 使用默认配置获得流程图表生成器，并生成追踪图片字符流
        InputStream imageStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", highLightedActiveIds, highLightedFlowIds, "宋体", "微软雅黑", "黑体", null, 2.0);


        File file = new File("heightLightDiagram.png");
        if(!file.exists()){
            file.createNewFile();
        }

        try(FileOutputStream outputStream = new FileOutputStream(file)){
            // 输出图片内容
            byte[] b = new byte[1024];
            int len;
            while ((len = imageStream.read(b, 0, 1024)) != -1) {
                outputStream.write(b, 0, len);
            }
            log.info("图片生成成功!");
        }

    }


    /**
     * 获取已经流转的线
     *
     * @param bpmnModel
     * @param historicActivityInstances
     * @return
     */
    private static List<String> getHighLightedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
        // 高亮流程已发生流转的线id集合
        List<String> highLightedFlowIds = new ArrayList<>();
        // 全部活动节点
        List<FlowNode> historicActivityNodes = new ArrayList<>();
        // 已完成的历史活动节点
        List<HistoricActivityInstance> finishedActivityInstances = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            historicActivityNodes.add(flowNode);
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstances.add(historicActivityInstance);
            }
        }

        FlowNode currentFlowNode = null;
        FlowNode targetFlowNode = null;
        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        for (HistoricActivityInstance currentActivityInstance : finishedActivityInstances) {
            // 获得当前活动对应的节点信息及outgoingFlows信息
            currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> sequenceFlows = currentFlowNode.getOutgoingFlows();

            /**
             * 遍历outgoingFlows并找到已已流转的 满足如下条件认为已已流转： 1.当前节点是并行网关或兼容网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最早的流转节点视为有效流转
             */
            if ("parallelGateway".equals(currentActivityInstance.getActivityType()) || "inclusiveGateway".equals(currentActivityInstance.getActivityType())) {
                // 遍历历史活动节点，找到匹配流程目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(), true);
                    if (historicActivityNodes.contains(targetFlowNode)) {
                        highLightedFlowIds.add(targetFlowNode.getId());
                    }
                }
            } else {
                List<Map<String, Object>> tempMapList = new ArrayList<>();
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
                        if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("highLightedFlowId", sequenceFlow.getId());
                            map.put("highLightedFlowStartTime", historicActivityInstance.getStartTime().getTime());
                            tempMapList.add(map);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(tempMapList)) {
                    // 遍历匹配的集合，取得开始时间最早的一个
                    long earliestStamp = 0L;
                    String highLightedFlowId = null;
                    for (Map<String, Object> map : tempMapList) {
                        long highLightedFlowStartTime = Long.valueOf(map.get("highLightedFlowStartTime").toString());
                        if (earliestStamp == 0 || earliestStamp >= highLightedFlowStartTime) {
                            highLightedFlowId = map.get("highLightedFlowId").toString();
                            earliestStamp = highLightedFlowStartTime;
                        }
                    }

                    highLightedFlowIds.add(highLightedFlowId);
                }

            }

        }
        return highLightedFlowIds;
    }

}
