package com.rl.activiti.test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import com.rl.model.LeaveBill;
/**
 * ���̱���
 */
public class ActivitiTest4 {

	RepositoryService repositoryService;
	
	RuntimeService runtimeService;
	
	TaskService taskService;
	
	@Before
	public void setUp() throws Exception {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
		taskService = processEngine.getTaskService();
	}

	/**
	 * ����ʵ���ϵı���
	 */
	@Test
	public void startProcessInstanceWithVariables() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("�������", 3);
		map.put("���ԭ��", "����ȥ");
		map.put("��ٿ�ʼʱ��", new Date());
		//������ʵ������ӱ�����Ӱ��ķ�Χ����ǰ����ʵ���µ����������ܻ�ȡ�����̱���������ִ�е����̶���Ҳ�ܻ�õ�����
		//������ʵ������ʱ���̱�����ʧ������ִ�еĶ�����ʧ�����̱�����ʷ��������
		//Ӱ�쵽�ı�select * from act_ru_variable; -- �洢�����ı�
			  //select * from act_hi_varinst; -- ���̱�������ʷ��
		runtimeService.startProcessInstanceByKey("activit_leave", map);
		
	}
	@Test
	public void startProcessInstance() {
		runtimeService.startProcessInstanceByKey("activit_leave");
		
	}
	
	/**
	 * ����ʵ������ı�����������ʵ������ʱ�Ͱѱ����ŵ�����ִ�е����̶����У�
	 */
	@Test
	public void completeTask(){
		String assignee = "boss";
		List<Task> taskList = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.orderByTaskCreateTime()
				.desc()
				.list();
		Task task = taskList.get(0);
		
		//�ڵ�ǰ�����л�����̱���
		Map<String, Object> map = taskService.getVariables(task.getId());
		System.out.println(map);
		
		/*List<Execution> executionList = runtimeService.createExecutionQuery().processDefinitionKey("activit_leave")
		.orderByProcessInstanceId().asc().list();
		Execution execution = executionList.get(0);
		//������ִ�е����̶����л�ȡ����
		Map<String, Object> map1 = runtimeService.getVariables(execution.getId());
		System.out.println(map1);*/
		taskService.complete(task.getId());
	}
	
	/**
	 * �ڵ�ǰ���������洢���̱��������̵ı����洢������ִ�е����̶����ϣ���ǰ����������������Ͷ����õ����̱���
	 */
	@Test
	public void putVariablesIntask(){
		String assignee = "employnee";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		
		//�������̱���
		taskService.setVariable(task.getId(), "�������", 10);
		taskService.setVariable(task.getId(), "���ԭ��", "���");
		taskService.setVariable(task.getId(), "��ʼʱ��", new Date());
		
		/*
		 * setVariableLocal:���̱���ֻ�ڵ�ǰ������Ч�����������ɾ���ʧ
		 * taskService.setVariableLocal(task.getId(), "�������", 10);
		taskService.setVariableLocal(task.getId(), "���ԭ��", "���");
		taskService.setVariableLocal(task.getId(), "��ʼʱ��", new Date());*/
		
		/*Map<String, Object> map = new HashMap<String, Object>();
		map.put("�������", 3);
		map.put("���ԭ��", "����ȥ");
		map.put("��ٿ�ʼʱ��", new Date());
		//���������ʱ���������̱���
		taskService.complete(task.getId(), map);*/
		
	}
	
	/**
	 * ������̱���
	 */
	@Test
	public void getVariablesIntask(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		//������̱���
		Integer days = (Integer) taskService.getVariable(task.getId(), "�������");
		String reason = (String) taskService.getVariable(task.getId(), "���ԭ��");
		Date startTime = (Date) taskService.getVariable(task.getId(), "��ʼʱ��");
		System.out.println(days);
		System.out.println(reason);
		System.out.println(startTime);
	}
	
	@Test
	public void getVariablesIntaskWithMap(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		//������̱���
		Map<String, Object> map = taskService.getVariables(task.getId());
		System.out.println(map);
	}
	
	@Test
	public void completeTask1(){
		String assignee = "employnee";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		taskService.complete(task.getId());
	}
	
	/**
	 * ͨ���������ӿ�����������ִ�ж����з��Զ����ʵ�����ʵ������Ҫ���л����һ�Ҫ����UID�������л���Ҫ��
	 */
	@Test
	public void putVariablesIntaskObject(){
		String assignee = "employnee";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		
		LeaveBill leaveBill = new LeaveBill();
		leaveBill.setId(1);
		leaveBill.setStartTime(new Date());
		leaveBill.setDays("2");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("��ٵ�����", leaveBill);
		
		//���������ʱ���������̱���
		taskService.complete(task.getId(), map);
		
	}
	
	/**
	 * ͨ����������ȡ�Զ���ʵ������̱���
	 */
	@Test
	public void getVariablesIntaskObjectWithMap(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		//������̱���
		Map<String, Object> map = taskService.getVariables(task.getId());
		LeaveBill leaveBill = (LeaveBill) map.get("��ٵ�����");
		System.out.println(leaveBill);
	}
	
	
	
}
