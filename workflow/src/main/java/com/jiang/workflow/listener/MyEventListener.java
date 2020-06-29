package com.jiang.workflow.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

/**
 * @author shijiang.luo
 * @create 2020/6/29
 */
public class MyEventListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent event) {
        switch (event.getType()) {
            case JOB_EXECUTION_SUCCESS:
                System.out.println("A job well done!");
                break;
            case JOB_EXECUTION_FAILURE:
                System.out.println("A job has failed...");
                break;
            default:
                System.out.println("Event received: " + event.getType());
        }
    }

    /**
     *
     * 决定了当事件分发时， onEvent（..）抛出异常时的行为
     * 这里返回的 是 false ，会忽略异常。 当返回 true 时，异常不会忽略，继续向上传播，迅速导致当前命令失
     * 败。 当事件是一个API调用的一部分时（或其他事务性操作，比如job执行）， 事务就会回
     * 滚。当事件监听器中的行为不是业务性时，建议返回 false
     *
     * @return
     */
    @Override
    public boolean isFailOnException() {
        return false;
    }
}
