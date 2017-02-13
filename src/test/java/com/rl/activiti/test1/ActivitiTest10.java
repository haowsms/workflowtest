package com.rl.activiti.test1;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
/**
 * �û��������
 * @author renliang
 *
 */
public class ActivitiTest10 {

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
		deploymentBuilder.addClasspathResource("diagrams9/candidate2.bpmn")
						 .name("�����������ʽ����")
						 .category("����")
						 .addClasspathResource("diagrams9/candidate2.png");
		//��������
		deploymentBuilder.deploy();
	}
	
	
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("candidate2");
	}
	
	
	@Test
	public void getCandidateTask(){
		String candidate = "Сǿ";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("candidate2")
				.taskCandidateUser(candidate)
				.singleResult();
		System.out.println("����ID��"+task.getId());
		System.out.println("�������ƣ�"+task.getName());
	}
	
	/**
	 * ����ӹ�,���������ɸ������� ���񱻽ӹܺ��������Ա�Ͳ����ٲ�ѯ������
	 */
	@Test
	public void getTask(){
		String candidate = "С��";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("candidate2")
				.taskCandidateUser(candidate)
				.singleResult();
		System.out.println("����ID��"+task.getId());
		System.out.println("�������ƣ�"+task.getName());
//		taskService.claim(task.getId(), candidate);
	}
	
	/**
	 * ������黹���黹���������Ա���Բ�ѯ��������
	 */
	@Test
	public void returnTask(){
		String assignee = "С��";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("candidate2")
				.taskAssignee(assignee)
				.singleResult();
		taskService.claim(task.getId(), null);
	}
	
	@Test
	public void getIdentityLink(){
		String taskId = "9804";
		List<IdentityLink> IdentityLinkList = taskService.getIdentityLinksForTask(taskId);
		for(IdentityLink identityLink : IdentityLinkList){
			System.out.println("��ѡ�ˣ�"+ identityLink.getUserId());
			System.out.println("����ʵ��ID��"+ identityLink.getProcessInstanceId());
		}
	}
	
	

}
