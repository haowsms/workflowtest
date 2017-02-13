package com.rl.activiti.test;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Before;
import org.junit.Test;
/**
 * ��ʷ��ѯ
 *
 */
public class ActivitiTest5 {

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
	 * ��ѯ��ʷ������ʵ��
	 */
	@Test
	public void selectHistoryProcessInstance() {
		List<HistoricProcessInstance> historicProcessInstanceList = historyService
				.createHistoricProcessInstanceQuery()//������ѯ��ʷ����ʵ���Ķ���
		.processDefinitionKey("activit_leave")
		.orderByProcessInstanceEndTime()
		.desc()
		.list();
		
		for(HistoricProcessInstance historicProcessInstance : historicProcessInstanceList){
			System.out.println("��ʷ����ʵ��ID��"+historicProcessInstance.getId());
			System.out.println("��ʷ����ʵ�����ʱ�䣺"+historicProcessInstance.getEndTime());
		}
		
	}
	
	/**
	 * ��ѯ��ǰ������ʵ�����������еĻ
	 */
	@Test
	public void selectHistoryActivityInstance() {
		List<HistoricActivityInstance> HistoricActivityInstance = historyService.createHistoricActivityInstanceQuery()
		.processInstanceId("3001")
		//.activityType("userTask")
		.list();
		
		for(HistoricActivityInstance historicActivityInstance : HistoricActivityInstance){
			System.out.println("��ʷ�ID��"+historicActivityInstance.getId());
			System.out.println("��ʷ����ƣ�"+historicActivityInstance.getActivityName());
			System.out.println("�����ˣ�"+historicActivityInstance.getAssignee());
		}
		
	}
	
	/**
	 * ���ݰ����˲�ѯ������ʷ����
	 */
	@Test
	public void selectHistoryTaskInstanceByAssignee() {
		List<HistoricTaskInstance> historicTaskInstanceList =  historyService.createHistoricTaskInstanceQuery()
		.processDefinitionKey("activit_leave")
		.taskAssignee("employnee")
		.list();
		
		for(HistoricTaskInstance historicTaskInstance : historicTaskInstanceList){
			System.out.println("��ʷ����ID��"+historicTaskInstance.getId());
			System.out.println("��ʷ����ƣ�"+historicTaskInstance.getName());
			System.out.println("����ʱ�䣺"+historicTaskInstance.getEndTime());
			System.out.println("�����ˣ�"+historicTaskInstance.getAssignee());
			System.out.println("----------------------------------------------------");
		}
		
	}
	
	/**
	 * ��������ʵ����ID����ѯ��ʷ���̱���
	 */
	@Test
	public void selectHistoryVariable() {
		List<HistoricVariableInstance> historicVariableInstanceList = historyService
				.createHistoricVariableInstanceQuery()
				.processInstanceId("1001")
				.list();
		
		for(HistoricVariableInstance historicVariableInstance : historicVariableInstanceList){
			System.out.println("���̱���ID��"+historicVariableInstance.getId());
			System.out.println("��������"+historicVariableInstance.getVariableName());
			System.out.println("����ֵ��"+historicVariableInstance.getValue());
			System.out.println("----------------------------------------------------");
		}
		
	}
	
	
	
	
	
	
}
