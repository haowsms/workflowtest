package com.rl.activiti.test;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
/**
 * �������
 *
 */
public class ActivitiTest3 {

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
	 * ���ݰ���������ѯ������������
	 */
	@Test
	public void queryTaksByAssignee() {
		String assignee = "boss";
		List<Task> taskList = taskService.createTaskQuery()//���������ѯ����
		.processDefinitionKey("activit_leave")//�������̶����key����ѯ
		.taskAssignee(assignee)//���ݰ���������ѯ
		.orderByTaskCreateTime()//����ʱ�䵹��
		.desc()
		.list();
		
		for(Task task : taskList){
			System.out.println("�����ID��"+task.getId());
			System.out.println("��������֣�"+task.getName());
			System.out.println("����Ĵ���ʱ�䣺"+task.getCreateTime());
			System.out.println("�����ˣ�"+task.getAssignee());
		}
		
	}
	
	/**
	 * ��ѯ����ִ�е����̶���
	 */
	@Test
	public void queryExecution() {
		List<Execution> executionList = runtimeService.createExecutionQuery()//��������ִ�е����̶���Ĳ�ѯ����
					.processDefinitionKey("activit_leave")//�������̶����Key����ѯ
					.list();
		for(Execution execution : executionList){
			System.out.println("����ִ�е����̶���Id��"+execution.getId());
			System.out.println("���ڻ�Ľڵ�Id��"+execution.getActivityId());
			System.out.println("��������ʵ��Id��"+execution.getProcessInstanceId());
		}
	}
	
	
	/**
	 * 
	 * ��������������û�н���ʱ��
	 * select * from act_ru_execution; -- ���ڻ��ACT_ID_ָ����һ���ڵ�
		select * from act_hi_procinst; -- ����������һ���ڵ�û�б仯
		select * from act_ru_task; -- ɾ����ǰʵ����һ�������񣬲�����ǰ������
		select * from act_hi_taskinst; -- ����һ����ǰ���������
		select * from act_hi_actinst; -- ����һ����ǰ���������
		
		�������������̽���ʱ��
	 * select * from act_ru_execution; -- �������е����̶�����ʧ
		select * from act_hi_procinst; -- ��ʷ������ʵ����ɣ�endtime����
		select * from act_ru_task; -- ��ǰʵ����������ʧ
		select * from act_hi_taskinst; -- û�б仯,���һ�������endtime����
		select * from act_hi_actinst; -- ����һ�������Ļendevent
	 */
	
	@Test
	public void completeTaksByAssignee() {
		String assignee = "boss";
		List<Task> taskList = taskService.createTaskQuery()//���������ѯ����
		.processDefinitionKey("activit_leave")//�������̶����key����ѯ
		.taskAssignee(assignee)//���ݰ���������ѯ
		.orderByTaskCreateTime()//����ʱ�䵹��
		.desc()
		.list();
		
		//�õ���һ������
		Task task = taskList.get(0);
		//ʹ���������ӿ�����������
		taskService.complete(task.getId());
	}
	
	@Test
	public void queryProcessInstanceState() {
		
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId("5001")
				.singleResult();
		if(processInstance == null){
			System.out.println("��ǰ�����Ѿ����");
		}else{
			System.out.println("��ǰ����ʵ��ID��"+processInstance.getId());
			System.out.println("��ǰ����������λ�ã�"+processInstance.getActivityId());
		}
		
	}
}
