package com.jiang.workflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * @author Bryan.luo
 * @CreateBy 2021/1/3 星期日
 * @description:
 *  执行监听器， 主要有三种时间类型:
 *  start（开始时触发）、
 *  end(结束时触发)、
 *  take（主要用于监控流程线，当流程流转改线时触发）
 **/
@Slf4j
@Component
public class ExecutionListenerImpl implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
       String event = execution.getEventName();
       switch (event){
           case "start":
               log.info("start event");
               break;
           case "end":
               log.info("end event");
               break;
           case "take":
               log.info("take event");
               break;
       }
    }
}
