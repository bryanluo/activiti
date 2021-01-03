package com.jiang.workflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * @author Bryan.luo
 * @CreateBy 2021/1/3 星期日
 * @description:
 *
 * 任务监听器：
 *
 *  该监听器只监听带有任务信息的节点，开始节点和结束节点由于在 Activiti 中没有任务因此无法监听
 *  支持四种监听事件:
 *  create: 任务创建时触发，此时所有属性已被设置完毕
 *  assignment: 任务被委派给某人后触发， 如通过变量的方式设置委托人时会触发， 优先于 create 事件
 *  complete： 在任务完成后，且被从运行时数据中（runtime data）删除前触发
 *  delete: 在任务将要被删除之前发生
 **/
@Slf4j
@Component
public class TaskListenerImpl implements TaskListener {

    @Override
    public void notify(DelegateTask task) {
       log.info("event: {} , info: {}", task.getEventName(), task.toString());
    }
}
