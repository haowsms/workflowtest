package com.rl.activiti.test1;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
/**
 * 用户任务分配
 * @author renliang
 *
 */
public class ActivitiTest10 {

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
		deploymentBuilder.addClasspathResource("diagrams9/candidate2.bpmn")
						 .name("组任务变量形式分配")
						 .category("分配")
						 .addClasspathResource("diagrams9/candidate2.png");
		//部署流程
		deploymentBuilder.deploy();
	}
	
	
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("candidate2");
	}
	
	
	@Test
	public void getCandidateTask(){
		String candidate = "小强";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("candidate2")
				.taskCandidateUser(candidate)
				.singleResult();
		System.out.println("任务ID："+task.getId());
		System.out.println("任务名称："+task.getName());
	}
	
	/**
	 * 任务接管,把组任务变成个人任务， 任务被接管后组任务成员就不能再查询到任务
	 */
	@Test
	public void getTask(){
		String candidate = "小丽";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("candidate2")
				.taskCandidateUser(candidate)
				.singleResult();
		System.out.println("任务ID："+task.getId());
		System.out.println("任务名称："+task.getName());
//		taskService.claim(task.getId(), candidate);
	}
	
	/**
	 * 组任务归还，归还后组任务成员可以查询到组任务
	 */
	@Test
	public void returnTask(){
		String assignee = "小丽";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("candidate2")
				.taskAssignee(assignee)
				.singleResult();
		taskService.claim(task.getId(), null);
	}
	
	@Test
	public void getIdentityLink(){
		String taskId = "9804";
		List<IdentityLink> IdentityLinkList = taskService.getIdentityLinksForTask(taskId);
		for(IdentityLink identityLink : IdentityLinkList){
			System.out.println("候选人："+ identityLink.getUserId());
			System.out.println("流程实例ID："+ identityLink.getProcessInstanceId());
		}
	}
	
	

}
