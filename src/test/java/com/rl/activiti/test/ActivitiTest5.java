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
 * 历史查询
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
	 * 查询历史的流程实例
	 */
	@Test
	public void selectHistoryProcessInstance() {
		List<HistoricProcessInstance> historicProcessInstanceList = historyService
				.createHistoricProcessInstanceQuery()//创建查询历史流程实例的对象
		.processDefinitionKey("activit_leave")
		.orderByProcessInstanceEndTime()
		.desc()
		.list();
		
		for(HistoricProcessInstance historicProcessInstance : historicProcessInstanceList){
			System.out.println("历史流程实例ID："+historicProcessInstance.getId());
			System.out.println("历史流程实例完成时间："+historicProcessInstance.getEndTime());
		}
		
	}
	
	/**
	 * 查询当前的流程实例所经历所有的活动
	 */
	@Test
	public void selectHistoryActivityInstance() {
		List<HistoricActivityInstance> HistoricActivityInstance = historyService.createHistoricActivityInstanceQuery()
		.processInstanceId("3001")
		//.activityType("userTask")
		.list();
		
		for(HistoricActivityInstance historicActivityInstance : HistoricActivityInstance){
			System.out.println("历史活动ID："+historicActivityInstance.getId());
			System.out.println("历史活动名称："+historicActivityInstance.getActivityName());
			System.out.println("办理人："+historicActivityInstance.getAssignee());
		}
		
	}
	
	/**
	 * 根据办理人查询他的历史任务
	 */
	@Test
	public void selectHistoryTaskInstanceByAssignee() {
		List<HistoricTaskInstance> historicTaskInstanceList =  historyService.createHistoricTaskInstanceQuery()
		.processDefinitionKey("activit_leave")
		.taskAssignee("employnee")
		.list();
		
		for(HistoricTaskInstance historicTaskInstance : historicTaskInstanceList){
			System.out.println("历史任务ID："+historicTaskInstance.getId());
			System.out.println("历史活动名称："+historicTaskInstance.getName());
			System.out.println("结束时间："+historicTaskInstance.getEndTime());
			System.out.println("办理人："+historicTaskInstance.getAssignee());
			System.out.println("----------------------------------------------------");
		}
		
	}
	
	/**
	 * 根据流程实例的ID来查询历史流程变量
	 */
	@Test
	public void selectHistoryVariable() {
		List<HistoricVariableInstance> historicVariableInstanceList = historyService
				.createHistoricVariableInstanceQuery()
				.processInstanceId("1001")
				.list();
		
		for(HistoricVariableInstance historicVariableInstance : historicVariableInstanceList){
			System.out.println("流程变量ID："+historicVariableInstance.getId());
			System.out.println("变量名："+historicVariableInstance.getVariableName());
			System.out.println("变量值："+historicVariableInstance.getValue());
			System.out.println("----------------------------------------------------");
		}
		
	}
	
	
	
	
	
	
}
