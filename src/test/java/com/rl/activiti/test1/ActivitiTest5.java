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
 * 发送任务
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
		//创建部署对象
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//加载流程的配置文件和图片
		deploymentBuilder.addClasspathResource("diagrams5/receviceTask.bpmn")
						 .name("汇总")
						 .category("利润")
						 .addClasspathResource("diagrams5/receviceTask.png");
		//部署流程
		deploymentBuilder.deploy();
	}
	
	
	/**
	 * select * from act_ru_execution; -- 产生一条数据
		select * from act_ru_task; -- 没有数据
		select * from act_hi_taskinst; -- 没有数据
		select * from act_hi_procinst; -- 产生一条数据
		select * from act_hi_actinst; -- 产生2条
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
			System.out.println("正在执行的流程对象ID:"+execution.getId());
			System.out.println("正在执行的流程对象名:"+execution.getActivityId());
		}
	}
	/**
	 * 汇总销售额
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
			//去汇总了
			map.put("totalCash", 10000);
			//让流程往下流
			runtimeService.signal(executionList.get(0).getId(), map);
		}
		
		
	}
	/**
	 * 发送短信给老板
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
			//发短信给老板
			System.out.println(totalCash);
			//让流程执行完毕
			runtimeService.signal(execution.getId());
		}
		
		
	}
	
	
	

}
