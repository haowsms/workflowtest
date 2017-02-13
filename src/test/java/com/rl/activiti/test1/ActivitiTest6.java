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
public class ActivitiTest6 {

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
		deploymentBuilder.addClasspathResource("diagrams6/taskForAssignee.bpmn")
						 .name("�û��������")
						 .category("����")
						 .addClasspathResource("diagrams6/taskForAssignee.png");
		//��������
		deploymentBuilder.deploy();
	}
	
	
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("taskForAssignee");
	}
	
	
	@Test
	public void completeManagerTask(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("taskForAssignee")
				.taskAssignee(assignee)
				.singleResult();
		//�����̱�����ָ̬����һ���û�����İ�����
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", "boss");
		taskService.complete(task.getId(), map);
	}
	
	@Test
	public void getDynamicUserTask(){
		String assignee = "boss";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("taskForAssignee")
				.taskAssignee(assignee)
				.singleResult();
		System.out.println("����ID��"+task.getId());
		System.out.println("�������֣�"+task.getName());
		
		//taskService.complete(task.getId());
	}
	
	

}
