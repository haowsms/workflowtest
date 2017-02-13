package com.rl.activiti.test;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.junit.Before;
import org.junit.Test;
/**
 * ����ʵ������
 *
 */
public class ActivitiTest2 {

	RepositoryService repositoryService;
	
	RuntimeService runtimeService;
	
	@Before
	public void setUp() throws Exception {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
	}

	/**
	 * ��������
	 *  select * from act_ru_execution; -- �������е����̶���
		select * from act_hi_procinst; -- ��ʷ����ʵ����
		select * from act_ru_task; -- �����
		select * from act_hi_taskinst; -- ��ʷ����ʵ����
		select * from act_hi_actinst; -- ��ʷ���о����Ļ
	   ���key��ͬ�������β������ԭ�е�key�����������汾���ۼ�
	 */
	@Test
	public void startProcess() {
		//�������̣��������̶����key�������ͬ�����̶����ж������ô������߰汾����������������汾���Ը���id��������act_re_procdef���е�id��
		ProcessInstance processInstance = runtimeService.startProcessInstanceById("activit_leave:1:4");
		System.out.println("����ʵ��ID��"+processInstance.getId());
		System.out.println("���ڻ�Ľڵ�ID��"+processInstance.getActivityId());//���ڻ�����̽ڵ�
		System.out.println("���̶���ID��"+processInstance.getProcessDefinitionId());//���ڻ�����̽ڵ�
		
	}
	
	@Test
	public void queryProcessInstance(){
		String processDefinitionKey = "activit_leave";
		
		//��������ʵ����ѯ����
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		
		/*List<ProcessInstance> processInstanceList = processInstanceQuery
				.processDefinitionKey(processDefinitionKey)//�������̶����key��activit_leave������ѯ
				.orderByProcessDefinitionKey()//�������̶����key����
				.desc()//��������
				.list();
		
		
		for(ProcessInstance processInstance : processInstanceList){
			System.out.println("����ʵ��ID��"+processInstance.getId());
			System.out.println("���ڻ�Ľڵ�ID��"+processInstance.getActivityId());//���ڻ�����̽ڵ�
			System.out.println("���̶���ID��"+processInstance.getProcessDefinitionId());//���ڻ�����̽ڵ�
		}*/
		
		
		ProcessInstance processInstance1 = processInstanceQuery
				.processDefinitionKey(processDefinitionKey).singleResult();//�����ȷ�����ݿ��ѯ�Ľ��ֻ��һ��������singleResult���������ͻᱨ��
				
		System.out.println("����ʵ��ID��"+processInstance1.getId());
		System.out.println("���ڻ�Ľڵ�ID��"+processInstance1.getActivityId());//���ڻ�����̽ڵ�
		System.out.println("���̶���ID��"+processInstance1.getProcessDefinitionId());//���ڻ�����̽ڵ�
	}
	
	
	
	
	
	
	
	

}
