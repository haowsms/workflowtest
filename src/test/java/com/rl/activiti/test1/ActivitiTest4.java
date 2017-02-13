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
public class ActivitiTest4 {

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
		deploymentBuilder.addClasspathResource("diagrams4/buy_sell.bpmn")
						 .name("����")
						 .category("����")
						 .addClasspathResource("diagrams4/buy_sell.png");
		//��������
		deploymentBuilder.deploy();
	}
	
	
	/**��֧��
	 * select * from act_ru_execution; -- ������������ݣ�һ����������ִ�ж���������������ִ�ж���ʹ��parent_id����ʶ��
		select * from act_ru_task; -- ������������
		select * from act_hi_taskinst; -- ������������
		select * from act_hi_procinst; -- ����һ������ʵ��
		select * from act_hi_actinst; -- ����4������
	 */
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("buy_sell");
	}
	
	/**���
	 * ���������ص�һ��·��ִ����ϣ��ڶ���û�����
	 *  select * from act_ru_execution; -- ������������ݣ�һ����������ִ�ж���������������ִ�ж���ʹ��parent_id����ʶ���� ����һ��ִ�ж����ߵ��ڶ����������أ��ڵȴ�
		select * from act_ru_task; -- �����һ��������ʧһ����
		select * from act_hi_taskinst; -- ����һ������
		select * from act_hi_procinst; -- û�б仯
		select * from act_hi_actinst; -- ����һ������
		
		
		���������ص�һ��·��ִ����ϣ��ڶ���Ҳ���
	 *  select * from act_ru_execution; -- û������
		select * from act_ru_task; -- û������
		select * from act_hi_taskinst; -- ����һ������
		select * from act_hi_procinst; -- endtime����
		select * from act_hi_actinst; -- ����2������
	 */
	@Test
	public void getTaskByAssignee(){
		String assignee = "getItemer";
		List<Task> taskList = taskService.createTaskQuery()
		.processDefinitionKey("buy_sell")
		.taskAssignee(assignee)
		.orderByTaskCreateTime()
		.desc()
		.list();
		
		
		taskService.complete(taskList.get(0).getId());
	}
	
	
	
	

}
