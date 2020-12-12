package com.jiang.workflow;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WorkflowApplicationTests {

	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;

	@Test
	void contextLoads() {
	}

	/**

	 1、部署流程的资源文件
	    上传到数据库中，该操作： ACT_RE_DEPLOYMENT 会生成一条数据
                             ACT_RE_PROCDEF 生成流程定义信息

     2、deployment 和 procdef 表一对多关系
     在 procdef 表中可以有多条记录，每条记录对应一个流程的定义信息

	 */
	@Test
	public void testDeployment(){
		// 使用 repositoryService 进行流程部署，定义一个流程名字
		Deployment deployment = repositoryService.createDeployment()
				.name("出差流程")
				.addClasspathResource("activiti/helloworld.bpmn")
				.addClasspathResource("activiti/helloworld.png")
				.deploy();
		System.out.println("流程部署ID: " + deployment.getId());
		System.out.println("流程部署名称: " + deployment.getName());
	}

	/**
	 * 根据流程定义id启动流程
	 *
	 *
	 */
	@Test
	public void startProcess(){
        ProcessInstance instance =  runtimeService.startProcessInstanceByKey("helloworld");
        System.out.println("流程定义ID: " + instance.getProcessDefinitionId());
        System.out.println("流程实例ID: " + instance.getId());
        System.out.println("当前活动的ID: " + instance.getActivityId());
	}

	/**
	 *  查询个人待执行的任务
	 */
	@Test
	public void queryTaskList(){
		List<Task> taskList = taskService.createTaskQuery()
				.taskAssignee("张三")
				.processDefinitionKey("helloworld").list();
		taskList.forEach(task -> {
			System.out.println("任务实例ID: " + task.getProcessDefinitionId());
			System.out.println("任务ID: " + task.getId());
			System.out.println("任务负责人: " + task.getAssignee());
			System.out.println("任务实例ID: " + task.getName());
		});
	}

	/**
	 *
	 * 任务处理
	 *
	 * 任务处理背后执行的表：
	 ACT_RU_TASK，ACT_RU_IDENTITYLINK，ACT_RU_EXECUTION，ACT_HI_TASKINST，ACT_HI_IDENTITYLINK
	 */
	@Test
	public void executeTask(){
		List<Task> taskList = taskService.createTaskQuery()
				.taskAssignee("张三")
				.processDefinitionKey("helloworld")
				.list();
		taskList.forEach(task -> {
			System.out.println("任务实例ID: " + task.getProcessDefinitionId());
			System.out.println("任务ID: " + task.getId());
			System.out.println("任务负责人: " + task.getAssignee());
			System.out.println("任务实例ID: " + task.getName());
			taskService.complete(task.getId());
			System.out.println("处理当前任务成功: " + task.getName());
			System.out.println("====================================");
		});
	}

}
