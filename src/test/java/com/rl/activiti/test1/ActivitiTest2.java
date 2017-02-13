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
 * flow（连接的线的管理）
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
		//创建部署对象
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//加载流程的配置文件和图片
		deploymentBuilder.addClasspathResource("diagrams2/leave_bill.bpmn")
						 .name("请假流程")
						 .category("请假")
						 .addClasspathResource("diagrams2/leave_bill.png");
		//部署流程
		deploymentBuilder.deploy();
	}
	
	
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("leave_bill");
	}
	
	/**
	 * 经理审批直接通过
	 */
	@Test
	public void completeTask(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("ruzhi")
				.taskAssignee(assignee)
				.singleResult();
		/**
		 * 通过流程变量的指定来决定走那条路线,一定要放到map中，在调用complete把map放到流程变量之中
		 */
		Map<String, Object> map = new HashMap<String,Object>();
		String outcome = "经理审批通过";
		map.put("outcome", outcome);
		taskService.complete(task.getId(), map);
	}
	
	/**
	 * 经理审批后发送给老板审批
	 */
	@Test
	public void completeTask1(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("leave_bill")
				.taskAssignee(assignee)
				.singleResult();
		Map<String, Object> map = new HashMap<String,Object>();
		String outcome = "经理驳回";
		map.put("outcome", outcome);
		taskService.complete(task.getId(), map);
	}
	
	/**
	 * 老板办理任务
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
		
		
		//获取流程定义的子类的对象
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(task.getProcessDefinitionId());
		//获取当前流程实例对象
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId())
				.singleResult();
		//获得正在执行的活动
		String activityId = processInstance.getActivityId();
		//获得正在执行的活动（包括流程图上所用的节点）
		ActivityImpl activityImpl = processDefinition.findActivity(activityId);
		//获得所有出去的线的对象
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
		
		
		//获取流程定义的子类的对象
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(task.getProcessDefinitionId());
		//获取当前流程实例对象
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId())
				.singleResult();
		//获得正在执行的活动
		String activityId = processInstance.getActivityId();
		//获得正在执行的活动（包括流程图上所用的节点）
		ActivityImpl activityImpl = processDefinition.findActivity(activityId);
		//获得所有出去的线的对象
		List<PvmTransition> transitionList = activityImpl.getIncomingTransitions();
		for(PvmTransition pvmTransition : transitionList){
			String flowName = (String) pvmTransition.getProperty("name");
			System.out.println(flowName);
			strList.add(flowName);
		}
		
		return strList;
	}
	
	/**
	 * 获得当前的活动节点的来路
	 * 第一步：获得当前节点的所有来路（其中一条是来路）
	 * 第二步：拿到上一步的节点（历史节点），找到它的所有出路（其中有一条出路是当前节点的来路）
	 * 第三步：找到以上两步中的重复的连线就是当前节点的来路
	 * 
	 * 
	 */
	@Test
	public void getIncomeingFlow(){
		//获得当前的任务
		String assignee = "employnee";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("leave_bill")
				.taskAssignee(assignee)
				.singleResult();
		List<String> preActivityOutcomeList = new ArrayList<String>();
		
		//获得当前的流程实例所经历的历史任务
		List<HistoricActivityInstance> historicActivityInstanceList = historyService
						.createHistoricActivityInstanceQuery()//创建当前实例历史任务的查询对象
						.processInstanceId(task.getProcessInstanceId())//根据当前实例的id来查询
						//.activityType("user_task")
						.orderByHistoricActivityInstanceStartTime()//根据当前实例历史获得开始时间倒序来排序
						.finished()//已经结束的活动节点
						.desc()
						.list();
		
		//输入历史活动
		for(HistoricActivityInstance historicActivityInstance : historicActivityInstanceList){
			System.out.println("历史活动ID：" + historicActivityInstance.getActivityId());
			System.out.println("历史活动Name：" + historicActivityInstance.getActivityName());
		}
		//获取前一个活动节点
		HistoricActivityInstance historicActivityInstance = historicActivityInstanceList.get(0);
		//获取活动ID
		String activitiyId = historicActivityInstance.getActivityId();
		//获得流程定义实体的对象（不需要创建查询对象******）
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(task.getProcessDefinitionId());
		//获取前一个活动节点的实现类的对象
		ActivityImpl activityImpl = processDefinition.findActivity(activitiyId);
		//获取到前一个节点的所有出路
		List<PvmTransition> transitionList = activityImpl.getOutgoingTransitions();
		for(PvmTransition pvmTransition : transitionList){
			String flowName = (String) pvmTransition.getProperty("name");
			System.out.println(flowName);
			preActivityOutcomeList.add(flowName);
		}
		
		//获得当前节点的所有入路
		List<String> incomeFlows = this.getIncomeFlow(assignee);
		String incomeFlow = null;
		for(String income : incomeFlows){
			for(String outcome : preActivityOutcomeList){
				if(income.equals(outcome)){
					incomeFlow = outcome;
				}
			}
		}
		System.out.println("当前节点的来路是："+incomeFlow);
		
	}
	
	
	

}
