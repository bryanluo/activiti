package com.jiang.workflow;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置任务候选人
 *
 * @author Bryan.luo
 * @CreateBy 2020/12/12 星期六
 * @description
 **/
public class CandidateUsersTest extends WorkflowApplicationTests {

    private final String processDefinitionKey = "candidateUsers";

    @Test
    public void deployment() {
        Deployment deployment = repositoryService.createDeployment()
                .name("一组候选人设置流程")
                .addClasspathResource("activiti/candidateUseres.bpmn")
                .deploy();
        System.out.println("流程部署ID: " + deployment.getId());
        System.out.println("流程部署名称: " + deployment.getName());
    }

    @Test
    public void setCandidateUsers() {
        Map<String, Object> candidateUserMap = new HashMap<>();
        candidateUserMap.put("assignee01", "江时");
        candidateUserMap.put("candidateUsers", "1,2,3");
        candidateUserMap.put("assignee03", "jiang");
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, candidateUserMap);
        System.out.println("流程定义ID: " + instance.getProcessDefinitionId());
        System.out.println("流程实例ID: " + instance.getId());
        System.out.println("当前活动的ID: " + instance.getActivityId());
    }

    /**
     * 组下的用户都可以查询
     */
    @Test
    public void queryCandidateTask() {
        String candidateUser = "jiang";
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateUser(candidateUser).list();
        taskList.forEach(task -> {
            System.out.println("查询当前组任务： " + task.getName());
            System.out.println("查询当前组任务： " + task.getAssignee()); // 为null，说明当前的用户只是候选人
            System.out.println("查询当前组任务： " + task.getProcessDefinitionId());
        });
    }

    /**
     * 拾取任务
     */
    @Test
    public void claim() {
        String candidateUser = "jiang";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateUser(candidateUser)
                .singleResult();
        System.out.println("任务名称： " + task.getName());
        System.out.println("开始拾取任务，把" + candidateUser + "变成负责人. ");
        // 参数1：任务ID，参数2：当前用户
        if (null != task) {
            taskService.claim(task.getId(), candidateUser);
            System.out.println("任务拾取完毕。");
        }
    }

    /**
     * 查询待办任务
     */
    @Test
    public void queryBacklogTask() {
        String assignee = "jiang";
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskAssignee(assignee)
                .list();
        taskList.forEach(task -> {
            System.out.println("查询当前组任务： " + task.getName());
            System.out.println("task assignee： " + task.getAssignee()); // 为null，说明当前的用户只是候选人
            System.out.println("prcessDefinitonId： " + task.getProcessDefinitionId());
        });
    }

    /**
     * 由负责人归还到组任务
     */
    @Test
    public void assigneeToGroupTask() {
        String candidateUser = "jiang";
        Task task = taskService.createTaskQuery()
                .taskAssignee(candidateUser)
                .processDefinitionKey(processDefinitionKey)
                .singleResult();
        System.out.println("任务名: " + task.getName());
        System.out.println("归还任务， 操作人: " + candidateUser);
        if (null != task) {
            taskService.setAssignee(task.getId(), null);
            System.out.println("归还任务成功！");
        }
    }


    @Test
    public void complete() {
        // 查询当前用户的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskAssignee("luo").singleResult();
        // 处理任务
        taskService.complete(task.getId());

        System.out.println("任务处理成功: " + task.getId());
    }


}
