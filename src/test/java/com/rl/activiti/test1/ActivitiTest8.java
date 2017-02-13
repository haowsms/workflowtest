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
 * �û��������
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
		//�����������
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//�������̵������ļ���ͼƬ
		deploymentBuilder.addClasspathResource("diagrams8/candidate.bpmn")
						 .name("���������")
						 .category("����")
						 .addClasspathResource("diagrams8/candidate.png");
		//��������
		deploymentBuilder.deploy();
	}
	
	
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("customer");
	}
	
	
	@Test
	public void getCandidateTask(){
		String candidate = "С��";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("customer")
				.taskCandidateUser(candidate)
				.singleResult();
		System.out.println("����ID��"+task.getId());
		System.out.println("�������ƣ�"+task.getName());
	}
	
	
	
	

}
