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
 * flow（连接的线的管理）
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
		//创建部署对象
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//加载流程的配置文件和图片
		deploymentBuilder.addClasspathResource("diagrams1/ruzhi.bpmn")
						 .name("入职审批")
						 .category("人力管理")
						 .addClasspathResource("diagrams1/ruzhi.png");
		//部署流程
		deploymentBuilder.deploy();
	}
	
	
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("ruzhi");
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
				.processDefinitionKey("ruzhi")
				.taskAssignee(assignee)
				.singleResult();
		Map<String, Object> map = new HashMap<String,Object>();
		String outcome = "发送老板审批";
		map.put("outcome", outcome);
		taskService.complete(task.getId(), map);
	}
	
	/**
	 * 老板办理任务
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
		}
	}
	@Test
	public void getIncomeFlow(){
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
		List<PvmTransition> transitionList = activityImpl.getIncomingTransitions();
		for(PvmTransition pvmTransition : transitionList){
			String flowName = (String) pvmTransition.getProperty("name");
			System.out.println(flowName);
		}
	}
	
	
	

}
