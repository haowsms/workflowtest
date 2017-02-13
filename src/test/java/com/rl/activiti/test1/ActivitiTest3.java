package com.rl.activiti.test1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
/**
 * ��������
 * @author renliang
 *
 */
public class ActivitiTest3 {

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

	
	/**
	 * �������أ�Ĭ�ϵ�flow���治��������
	 */
	@Test
	public void delpoyActiviti() {
		//�����������
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//�������̵������ļ���ͼƬ
		deploymentBuilder.addClasspathResource("diagrams3/baoxiao.bpmn")
						 .name("��������")
						 .category("����")
						 .addClasspathResource("diagrams3/baoxiao.png");
		//��������
		deploymentBuilder.deploy();
	}
	
	
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("baoxiao_bill");
	}
	
	@Test
	public void getTaskByAssignee(){
		String assignee = "account";
		Task task = taskService.createTaskQuery()
		.processDefinitionKey("baoxiao_bill")
		.taskAssignee(assignee)
		.singleResult();
		
		System.out.println("����ID��"+ task.getId());
		System.out.println("�������ƣ�"+ task.getName());
	}
	
	/**
	 * �������ظ��������Զ��������������ͼ�����س�·�����϶��壩
	 */
	@Test
	public void completeTask(){
		String assignee = "employnee";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("baoxiao_bill")
				.taskAssignee(assignee)
				.singleResult();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("money", 400);
		taskService.complete(task.getId(), map);
		//taskService.complete(task.getId());
		
	}
	
	
	

}
