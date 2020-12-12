package com.jiang.workflow;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shijiang
 *
 */
@MapperScan("com.jiang.workflow.domain.repository")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class WorkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkflowApplication.class, args);
	}

}
