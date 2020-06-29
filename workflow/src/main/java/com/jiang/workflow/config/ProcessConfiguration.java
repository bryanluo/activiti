package com.jiang.workflow.config;

import com.jiang.workflow.listener.MyEventListener;
import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * @author shijiang.luo
 * @create 2020/6/29
 */
@Configurable
public class ProcessConfiguration {

    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(ActivitiEventListener myEventListener){
        StandaloneProcessEngineConfiguration pec = new StandaloneProcessEngineConfiguration();
        List<ActivitiEventListener> activitiListeners =  new ArrayList<>();
        activitiListeners.add(myEventListener);
        pec.setEventListeners(activitiListeners);
        return pec;
    }

    @Bean
    public ActivitiEventListener myEventListener(){

        return  new MyEventListener();
    }

}
