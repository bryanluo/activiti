# activity 使用 API 记录

## 基本流程操作

### 1、流程部署

**流程部署过程会执行的表：**

```

# 将资源文件存储在该表
insert into ACT_GE_BYTEARRAY;
# 生成一条部署信息
insert into ACT_RE_DEPLOYMENT;
# 生成一条流程定
insert into ACT_RE_PROCDEF;
```

deployment 和 procdef 表是一对多关系，在 procdef 中可以有多条记录， 每条记录对应一个流程定义信息。

**流程部署的代码**

```java
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
```

### 2、流程启动

**启动流程会执行的表**

```
# 新增任务实例信息
insert into ACT_HI_TASKINST

# 对应生成一条流程实例
insert into ACT_HI_PROCINST

# 生成历史实例信息
insert into ACT_HI_ACTINST

# 生成待执行人历史记录
insert into ACT_HI_IDENTITYLINK

# 正在执行
insert into ACT_RU_EXECUTION

# 生成待执行任务
insert into ACT_RU_TASK

# 生成待执行任务负责人
insert into ACT_RU_IDENTITYLINK
```

**启动流程代码**

```java
@Test
public void startProcess(){
    ProcessInstance instance =  runtimeService.startProcessInstanceByKey("helloworld");
    System.out.println("流程定义ID: " + instance.getProcessDefinitionId());
    System.out.println("流程实例ID: " + instance.getId());
    System.out.println("当前活动的ID: " + instance.getActivityId());
}
```

### 3、查询个人待查询任务

**查询涉及SQL**

```sql
 select distinct RES.* from ACT_RU_TASK RES inner join ACT_RE_PROCDEF D on RES.PROC_DEF_ID_ = D.ID_ WHERE RES.ASSIGNEE_ = ? and D.KEY_ = ? order by RES.ID_ asc LIMIT ? OFFSET ? 
 
==> Parameters: 张三(String), helloworld(String), 2147483647(Integer), 0(Integer)
```

**查询待处理任务代码**

```java
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
```

### 4、处理任务

**涉及SQL**

```sql

insert into ACT_RU_TASK (ID_, REV_, NAME_, PARENT_TASK_ID_, DESCRIPTION_, PRIORITY_, CREATE_TIME_, OWNER_, ASSIGNEE_, DELEGATION_, EXECUTION_ID_, PROC_INST_ID_, PROC_DEF_ID_, TASK_DEF_KEY_, DUE_DATE_, CATEGORY_, SUSPENSION_STATE_, TENANT_ID_, FORM_KEY_, CLAIM_TIME_) values (?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) 

# 2、
insert into ACT_RU_IDENTITYLINK (ID_, REV_, TYPE_, USER_ID_, GROUP_ID_, TASK_ID_, PROC_INST_ID_, PROC_DEF_ID_) values (?, 1, ?, ?, ?, ?, ?, ?) 

# 3
update ACT_RU_EXECUTION set REV_ = ?, BUSINESS_KEY_ = ?, PROC_DEF_ID_ = ?, ACT_ID_ = ?, IS_ACTIVE_ = ?, IS_CONCURRENT_ = ?, IS_SCOPE_ = ?, IS_EVENT_SCOPE_ = ?, IS_MI_ROOT_ = ?, PARENT_ID_ = ?, SUPER_EXEC_ = ?, ROOT_PROC_INST_ID_ = ?, SUSPENSION_STATE_ = ?, NAME_ = ?, IS_COUNT_ENABLED_ = ?, EVT_SUBSCR_COUNT_ = ?, TASK_COUNT_ = ?, JOB_COUNT_ = ?, TIMER_JOB_COUNT_ = ?, SUSP_JOB_COUNT_ = ?, DEADLETTER_JOB_COUNT_ = ?, VAR_COUNT_ = ?, ID_LINK_COUNT_ = ? where ID_ = ? and REV_ = ? 

# 4、
update ACT_HI_ACTINST set EXECUTION_ID_ = ?, ASSIGNEE_ = ?, END_TIME_ = ?, DURATION_ = ?, DELETE_REASON_ = ? where ID_ = ? 

# 5、
update ACT_HI_TASKINST set PROC_DEF_ID_ = ?, EXECUTION_ID_ = ?, NAME_ = ?, PARENT_TASK_ID_ = ?, DESCRIPTION_ = ?, OWNER_ = ?, ASSIGNEE_ = ?, CLAIM_TIME_ = ?, END_TIME_ = ?, DURATION_ = ?, DELETE_REASON_ = ?, TASK_DEF_KEY_ = ?, FORM_KEY_ = ?, PRIORITY_ = ?, DUE_DATE_ = ?, CATEGORY_ = ? where ID_ = ? 

# 6、
delete from ACT_RU_TASK where ID_ = ? and REV_ = ? 

```

**处理任务代码**

```java
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
```


## 给任务绑定 bussnessKey

启动流程实例，添加 businessKey

本质： act_ru_execution表中的businessKey字段要存入业务标识

**代码**

```java
@Test
public void businessKeyAdd(){
    // 第一个参数是流程定义KEY
    // 第二个参数是业务标识businessKey
    ProcessInstance instance = runtimeService.startProcessInstanceByKey("helloworld", "这里写入请假单的ID");
    System.out.println(instance.getBusinessKey());
}
```


## 全部流程的激活与挂起

操作流程定义为挂起专题，该流程定义下边所有的流程实例全部暂停。流程定义为挂起状态该流程定义将不允许启动新的流程实例，同时该流程定义下所有的流程实例将全部挂起暂停执行。

**代码**

```java
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
```

## 动态设置负责人

**1、使用 UEL 设置**

在流程配置审批人的使用使用 ${assignee} ， 在代码上使用变量实现替换。 如下面的assignee01、assignee02、assignee03

代码：

```java
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
```

**2、候选人方式**

设置审批人有三种方式：assignee、candidateUser、candidateGroup

```
# 组任务办理流程

### 第一步：查询组任务
指定候选人， 查询该候选人的待办任务。
候选人不能办理任务

### 第二步： 拾取任务
改组任务的候选人都能拾取。
将候选人的组任务，变成个人任务。原来的候选人就变成了该任务的负责人。

***如果拾取后不想办理该任务？***

   需要将已经拾取的个人任务归还到组里边，将个人任务变成组任务。
   
### 第三步：查询个人任务
查询方式同个人任务部分，根据assignee查询用户负责的个人任务。

### 第四步： 办理个人任务
```

**配置bomn**

```xml
 <userTask activiti:candidateUsers="${candidateUsers}" activiti:exclusive="true" completionQuantity="1" id="_6" implementation="##unspecified" isForCompensation="false" name="审批【部门经理】" startQuantity="1">
      <incoming>_8</incoming>
      <outgoing>_9</outgoing>
    </userTask>
```

**动态设置候选人代码**

candidateUsers设置的使用以 ‘,’ 隔开

```java
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
```

**查询当前用户待办任务***

只要是候选人都能查询到待办任务

```java
/**
组下的用户都可以查询
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
```

**拾取任务**

将候选人转成负责人

```java
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
```

**成为负责人后查询待办任务**

```java
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
```

**由负责人归还到组任务**
```java
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
```