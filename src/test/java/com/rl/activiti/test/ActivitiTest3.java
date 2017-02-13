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
 * 任务管理
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
	 * 根据办理人来查询他的所有任务
	 */
	@Test
	public void queryTaksByAssignee() {
		String assignee = "boss";
		List<Task> taskList = taskService.createTaskQuery()//创建任务查询对象
		.processDefinitionKey("activit_leave")//根据流程定义的key来查询
		.taskAssignee(assignee)//根据办理人来查询
		.orderByTaskCreateTime()//根据时间倒序
		.desc()
		.list();
		
		for(Task task : taskList){
			System.out.println("任务的ID："+task.getId());
			System.out.println("任务的名字："+task.getName());
			System.out.println("任务的创建时间："+task.getCreateTime());
			System.out.println("办理人："+task.getAssignee());
		}
		
	}
	
	/**
	 * 查询正在执行的流程对象
	 */
	@Test
	public void queryExecution() {
		List<Execution> executionList = runtimeService.createExecutionQuery()//创建正在执行的流程对象的查询对象
					.processDefinitionKey("activit_leave")//根据流程定义的Key来查询
					.list();
		for(Execution execution : executionList){
			System.out.println("正在执行的流程对象Id："+execution.getId());
			System.out.println("正在活动的节点Id："+execution.getActivityId());
			System.out.println("所属流程实例Id："+execution.getProcessInstanceId());
		}
	}
	
	
	/**
	 * 
	 * 当办理任务（流程没有结束时）
	 * select * from act_ru_execution; -- 正在活动的ACT_ID_指到下一个节点
		select * from act_hi_procinst; -- 如果不是最后一个节点没有变化
		select * from act_ru_task; -- 删除当前实例上一步的任务，产生当前的任务
		select * from act_hi_taskinst; -- 增加一条当前任务的数据
		select * from act_hi_actinst; -- 增加一条当前任务的数据
		
		当办理任务（流程结束时）
	 * select * from act_ru_execution; -- 正在运行的流程对象消失
		select * from act_hi_procinst; -- 历史的流程实例完成，endtime出现
		select * from act_ru_task; -- 当前实例的任务消失
		select * from act_hi_taskinst; -- 没有变化,最后一个任务的endtime出现
		select * from act_hi_actinst; -- 增加一个结束的活动endevent
	 */
	
	@Test
	public void completeTaksByAssignee() {
		String assignee = "boss";
		List<Task> taskList = taskService.createTaskQuery()//创建任务查询对象
		.processDefinitionKey("activit_leave")//根据流程定义的key来查询
		.taskAssignee(assignee)//根据办理人来查询
		.orderByTaskCreateTime()//根据时间倒序
		.desc()
		.list();
		
		//拿到第一个任务
		Task task = taskList.get(0);
		//使用任务服务接口来办理任务
		taskService.complete(task.getId());
	}
	
	@Test
	public void queryProcessInstanceState() {
		
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId("5001")
				.singleResult();
		if(processInstance == null){
			System.out.println("当前流程已经完成");
		}else{
			System.out.println("当前流程实例ID："+processInstance.getId());
			System.out.println("当前流程所处的位置："+processInstance.getActivityId());
		}
		
	}
}
