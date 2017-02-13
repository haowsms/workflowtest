package com.rl.activiti.test1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
/**
 * flow�����ӵ��ߵĹ���
 * @author renliang
 *
 */
public class ActivitiTest1 {

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

	
	@Test
	public void delpoyActiviti() {
		//�����������
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//�������̵������ļ���ͼƬ
		deploymentBuilder.addClasspathResource("diagrams1/ruzhi.bpmn")
						 .name("��ְ����")
						 .category("��������")
						 .addClasspathResource("diagrams1/ruzhi.png");
		//��������
		deploymentBuilder.deploy();
	}
	
	
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("ruzhi");
	}
	
	/**
	 * ��������ֱ��ͨ��
	 */
	@Test
	public void completeTask(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("ruzhi")
				.taskAssignee(assignee)
				.singleResult();
		/**
		 * ͨ�����̱�����ָ��������������·��,һ��Ҫ�ŵ�map�У��ڵ���complete��map�ŵ����̱���֮��
		 */
		Map<String, Object> map = new HashMap<String,Object>();
		String outcome = "��������ͨ��";
		map.put("outcome", outcome);
		taskService.complete(task.getId(), map);
	}
	
	/**
	 * �����������͸��ϰ�����
	 */
	@Test
	public void completeTask1(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("ruzhi")
				.taskAssignee(assignee)
				.singleResult();
		Map<String, Object> map = new HashMap<String,Object>();
		String outcome = "�����ϰ�����";
		map.put("outcome", outcome);
		taskService.complete(task.getId(), map);
	}
	
	/**
	 * �ϰ��������
	 */
	@Test
	public void completeTask2(){
		String assignee = "boss";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("ruzhi")
				.taskAssignee(assignee)
				.singleResult();
		
		taskService.complete(task.getId());
	}
	
	@Test
	public void getOutcomeFlow(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("ruzhi")
				.taskAssignee(assignee)
				.singleResult();
		
		
		//��ȡ���̶��������Ķ���
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(task.getProcessDefinitionId());
		//��ȡ��ǰ����ʵ������
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId())
				.singleResult();
		//�������ִ�еĻ
		String activityId = processInstance.getActivityId();
		//�������ִ�еĻ����������ͼ�����õĽڵ㣩
		ActivityImpl activityImpl = processDefinition.findActivity(activityId);
		//������г�ȥ���ߵĶ���
		List<PvmTransition> transitionList = activityImpl.getOutgoingTransitions();
		for(PvmTransition pvmTransition : transitionList){
			String flowName = (String) pvmTransition.getProperty("name");
			System.out.println(flowName);
		}
	}
	@Test
	public void getIncomeFlow(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("ruzhi")
				.taskAssignee(assignee)
				.singleResult();
		
		
		//��ȡ���̶��������Ķ���
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(task.getProcessDefinitionId());
		//��ȡ��ǰ����ʵ������
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId())
				.singleResult();
		//�������ִ�еĻ
		String activityId = processInstance.getActivityId();
		//�������ִ�еĻ����������ͼ�����õĽڵ㣩
		ActivityImpl activityImpl = processDefinition.findActivity(activityId);
		//������г�ȥ���ߵĶ���
		List<PvmTransition> transitionList = activityImpl.getIncomingTransitions();
		for(PvmTransition pvmTransition : transitionList){
			String flowName = (String) pvmTransition.getProperty("name");
			System.out.println(flowName);
		}
	}
	
	
	

}
