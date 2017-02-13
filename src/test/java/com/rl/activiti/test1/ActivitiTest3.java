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
 * 排他网关
 * @author renliang
 *
 */
public class ActivitiTest3 {

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
	 * 排他网关，默认的flow上面不能有条件
	 */
	@Test
	public void delpoyActiviti() {
		//创建部署对象
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//加载流程的配置文件和图片
		deploymentBuilder.addClasspathResource("diagrams3/baoxiao.bpmn")
						 .name("报销流程")
						 .category("报销")
						 .addClasspathResource("diagrams3/baoxiao.png");
		//部署流程
		deploymentBuilder.deploy();
	}
	
	
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("baoxiao_bill");
	}
	
	@Test
	public void getTaskByAssignee(){
		String assignee = "account";
		Task task = taskService.createTaskQuery()
		.processDefinitionKey("baoxiao_bill")
		.taskAssignee(assignee)
		.singleResult();
		
		System.out.println("任务ID："+ task.getId());
		System.out.println("任务名称："+ task.getName());
	}
	
	/**
	 * 排他网关根据流程自定义变量（在流程图的网关出路的线上定义）
	 */
	@Test
	public void completeTask(){
		String assignee = "employnee";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("baoxiao_bill")
				.taskAssignee(assignee)
				.singleResult();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("money", 400);
		taskService.complete(task.getId(), map);
		//taskService.complete(task.getId());
		
	}
	
	
	

}
