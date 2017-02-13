package com.rl.activiti.test1;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
/**
 * 用户任务分配
 * @author renliang
 *
 */
public class ActivitiTest8 {

	RepositoryService repositoryService;
	
	RuntimeService runtimeService;
	
	TaskService taskService;
	
	HistoryService historyService;
	
	@Before
	public void setUp() throws Exception {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
		taskService = processEngine.getTaskService();
		historyService = processEngine.getHistoryService();
	}

	
	
	@Test
	public void delpoyActiviti() {
		//创建部署对象
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//加载流程的配置文件和图片
		deploymentBuilder.addClasspathResource("diagrams8/candidate.bpmn")
						 .name("组任务分配")
						 .category("分配")
						 .addClasspathResource("diagrams8/candidate.png");
		//部署流程
		deploymentBuilder.deploy();
	}
	
	
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("customer");
	}
	
	
	@Test
	public void getCandidateTask(){
		String candidate = "小黑";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("customer")
				.taskCandidateUser(candidate)
				.singleResult();
		System.out.println("任务ID："+task.getId());
		System.out.println("任务名称："+task.getName());
	}
	
	
	
	

}
