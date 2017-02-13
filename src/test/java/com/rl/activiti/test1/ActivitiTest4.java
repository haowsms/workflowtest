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
 * 并行网关
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
		//创建部署对象
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//加载流程的配置文件和图片
		deploymentBuilder.addClasspathResource("diagrams4/buy_sell.bpmn")
						 .name("买卖")
						 .category("销售")
						 .addClasspathResource("diagrams4/buy_sell.png");
		//部署流程
		deploymentBuilder.deploy();
	}
	
	
	/**分支：
	 * select * from act_ru_execution; -- 会产生三条数据，一条是主流程执行对象，两条是子流程执行对象（使用parent_id来标识）
		select * from act_ru_task; -- 产生两条数据
		select * from act_hi_taskinst; -- 产生两条数据
		select * from act_hi_procinst; -- 产生一个流程实例
		select * from act_hi_actinst; -- 产生4条数据
	 */
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("buy_sell");
	}
	
	/**汇合
	 * 当并行网关第一条路线执行完毕，第二条没有完成
	 *  select * from act_ru_execution; -- 会产生三条数据，一条是主流程执行对象，两条是子流程执行对象（使用parent_id来标识）， 其中一个执行对象走到第二个并行网关，在等待
		select * from act_ru_task; -- 完成了一个任务（消失一条）
		select * from act_hi_taskinst; -- 增加一条数据
		select * from act_hi_procinst; -- 没有变化
		select * from act_hi_actinst; -- 增加一条数据
		
		
		当并行网关第一条路线执行完毕，第二条也完成
	 *  select * from act_ru_execution; -- 没有数据
		select * from act_ru_task; -- 没有数据
		select * from act_hi_taskinst; -- 增加一条数据
		select * from act_hi_procinst; -- endtime出现
		select * from act_hi_actinst; -- 增加2条数据
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
