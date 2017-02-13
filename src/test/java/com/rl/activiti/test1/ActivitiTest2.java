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
 * flow�����ӵ��ߵĹ���
 * @author renliang
 *
 */
public class ActivitiTest2 {

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
		deploymentBuilder.addClasspathResource("diagrams2/leave_bill.bpmn")
						 .name("�������")
						 .category("���")
						 .addClasspathResource("diagrams2/leave_bill.png");
		//��������
		deploymentBuilder.deploy();
	}
	
	
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("leave_bill");
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
				.processDefinitionKey("leave_bill")
				.taskAssignee(assignee)
				.singleResult();
		Map<String, Object> map = new HashMap<String,Object>();
		String outcome = "������";
		map.put("outcome", outcome);
		taskService.complete(task.getId(), map);
	}
	
	/**
	 * �ϰ��������
	 */
	@Test
	public void completeTask2(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("leave_bill")
				.taskAssignee(assignee)
				.singleResult();
		taskService.complete(task.getId());
	}
	
	
	public List<String> getOutcomeFlow(){
		List<String> strList = new ArrayList<String>();
		
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
			strList.add(flowName);
		}
		return strList;
	}

	public List<String> getIncomeFlow(String assignee){
		List<String> strList = new ArrayList<String>();
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("leave_bill")
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
			strList.add(flowName);
		}
		
		return strList;
	}
	
	/**
	 * ��õ�ǰ�Ļ�ڵ����·
	 * ��һ������õ�ǰ�ڵ��������·������һ������·��
	 * �ڶ������õ���һ���Ľڵ㣨��ʷ�ڵ㣩���ҵ��������г�·��������һ����·�ǵ�ǰ�ڵ����·��
	 * ���������ҵ����������е��ظ������߾��ǵ�ǰ�ڵ����·
	 * 
	 * 
	 */
	@Test
	public void getIncomeingFlow(){
		//��õ�ǰ������
		String assignee = "employnee";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("leave_bill")
				.taskAssignee(assignee)
				.singleResult();
		List<String> preActivityOutcomeList = new ArrayList<String>();
		
		//��õ�ǰ������ʵ������������ʷ����
		List<HistoricActivityInstance> historicActivityInstanceList = historyService
						.createHistoricActivityInstanceQuery()//������ǰʵ����ʷ����Ĳ�ѯ����
						.processInstanceId(task.getProcessInstanceId())//���ݵ�ǰʵ����id����ѯ
						//.activityType("user_task")
						.orderByHistoricActivityInstanceStartTime()//���ݵ�ǰʵ����ʷ��ÿ�ʼʱ�䵹��������
						.finished()//�Ѿ������Ļ�ڵ�
						.desc()
						.list();
		
		//������ʷ�
		for(HistoricActivityInstance historicActivityInstance : historicActivityInstanceList){
			System.out.println("��ʷ�ID��" + historicActivityInstance.getActivityId());
			System.out.println("��ʷ�Name��" + historicActivityInstance.getActivityName());
		}
		//��ȡǰһ����ڵ�
		HistoricActivityInstance historicActivityInstance = historicActivityInstanceList.get(0);
		//��ȡ�ID
		String activitiyId = historicActivityInstance.getActivityId();
		//������̶���ʵ��Ķ��󣨲���Ҫ������ѯ����******��
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(task.getProcessDefinitionId());
		//��ȡǰһ����ڵ��ʵ����Ķ���
		ActivityImpl activityImpl = processDefinition.findActivity(activitiyId);
		//��ȡ��ǰһ���ڵ�����г�·
		List<PvmTransition> transitionList = activityImpl.getOutgoingTransitions();
		for(PvmTransition pvmTransition : transitionList){
			String flowName = (String) pvmTransition.getProperty("name");
			System.out.println(flowName);
			preActivityOutcomeList.add(flowName);
		}
		
		//��õ�ǰ�ڵ��������·
		List<String> incomeFlows = this.getIncomeFlow(assignee);
		String incomeFlow = null;
		for(String income : incomeFlows){
			for(String outcome : preActivityOutcomeList){
				if(income.equals(outcome)){
					incomeFlow = outcome;
				}
			}
		}
		System.out.println("��ǰ�ڵ����·�ǣ�"+incomeFlow);
		
	}
	
	
	

}
