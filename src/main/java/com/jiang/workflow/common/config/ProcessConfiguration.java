package com.jiang.workflow.common.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @author shijiang.luo
 * @create 2020/6/29
 */
@Configurable
public class ProcessConfiguration {

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration(DataSource dataSource){
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource);
        return processEngineConfiguration;
    }

}
