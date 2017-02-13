package com.rl.activiti.test1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
/**
 * ��������
 * @author renliang
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

	
	
	@Test
	public void delpoyActiviti() {
		//�����������
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//�������̵������ļ���ͼƬ
		deploymentBuilder.addClasspathResource("diagrams5/receviceTask.bpmn")
						 .name("����")
						 .category("����")
						 .addClasspathResource("diagrams5/receviceTask.png");
		//��������
		deploymentBuilder.deploy();
	}
	
	
	/**
	 * select * from act_ru_execution; -- ����һ������
		select * from act_ru_task; -- û������
		select * from act_hi_taskinst; -- û������
		select * from act_hi_procinst; -- ����һ������
		select * from act_hi_actinst; -- ����2��
	 */
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("myProcess");
	}
	
	@Test
	public void getExecutionObj(){
		List<Execution> executionList = runtimeService
				.createExecutionQuery()
				.processDefinitionKey("myProcess")
				.list();
		for(Execution execution : executionList){
			System.out.println("����ִ�е����̶���ID:"+execution.getId());
			System.out.println("����ִ�е����̶�����:"+execution.getActivityId());
		}
	}
	/**
	 * �������۶�
	 */
	@Test
	public void doExecutionObj(){
		List<Execution> executionList = runtimeService
				.createExecutionQuery()
				.processDefinitionKey("myProcess")
				.list();
		Execution execution = executionList.get(0);
		String activityId = execution.getActivityId();
		if(activityId.equals("totalSum")){
			Map<String, Object> map = new HashMap<String, Object>();
			//ȥ������
			map.put("totalCash", 10000);
			//������������
			runtimeService.signal(executionList.get(0).getId(), map);
		}
		
		
	}
	/**
	 * ���Ͷ��Ÿ��ϰ�
	 */
	@Test
	public void doExecutionObjsendMess(){
		List<Execution> executionList = runtimeService
				.createExecutionQuery()
				.processDefinitionKey("myProcess")
				.list();
		
		Execution execution = executionList.get(0);
		String activityId = execution.getActivityId();
		if(activityId.equals("sendMess")){
			Integer totalCash = (Integer) runtimeService.getVariable(execution.getId(), "totalCash");
			//�����Ÿ��ϰ�
			System.out.println(totalCash);
			//������ִ�����
			runtimeService.signal(execution.getId());
		}
		
		
	}
	
	
	

}
