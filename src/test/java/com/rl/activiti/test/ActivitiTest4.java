package com.rl.activiti.test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import com.rl.model.LeaveBill;
/**
 * 流程变量
 */
public class ActivitiTest4 {

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
	 * 流程实例上的变量
	 */
	@Test
	public void startProcessInstanceWithVariables() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("请假天数", 3);
		map.put("请假原因", "旅游去");
		map.put("请假开始时间", new Date());
		//在流程实例上添加变量：影响的范围，当前流程实例下的所有任务都能获取到流程变量，正在执行的流程对象也能获得到变量
		//当流程实例结束时流程变量消失，正在执行的对象消失，流程变量历史表保留变量
		//影响到的表select * from act_ru_variable; -- 存储变量的表
			  //select * from act_hi_varinst; -- 流程变量的历史表
		runtimeService.startProcessInstanceByKey("activit_leave", map);
		
	}
	@Test
	public void startProcessInstance() {
		runtimeService.startProcessInstanceByKey("activit_leave");
		
	}
	
	/**
	 * 流程实例级别的变量（在流程实例启动时就把变量放到正在执行的流程对象中）
	 */
	@Test
	public void completeTask(){
		String assignee = "boss";
		List<Task> taskList = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.orderByTaskCreateTime()
				.desc()
				.list();
		Task task = taskList.get(0);
		
		//在当前任务中获得流程变量
		Map<String, Object> map = taskService.getVariables(task.getId());
		System.out.println(map);
		
		/*List<Execution> executionList = runtimeService.createExecutionQuery().processDefinitionKey("activit_leave")
		.orderByProcessInstanceId().asc().list();
		Execution execution = executionList.get(0);
		//从正在执行的流程对象中获取变量
		Map<String, Object> map1 = runtimeService.getVariables(execution.getId());
		System.out.println(map1);*/
		taskService.complete(task.getId());
	}
	
	/**
	 * 在当前任务中来存储流程变量，流程的变量存储在正在执行的流程对象上，当前任务后面的所有任务就都能拿到流程变量
	 */
	@Test
	public void putVariablesIntask(){
		String assignee = "employnee";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		
		//设置流程变量
		taskService.setVariable(task.getId(), "请假天数", 10);
		taskService.setVariable(task.getId(), "请假原因", "结婚");
		taskService.setVariable(task.getId(), "开始时间", new Date());
		
		/*
		 * setVariableLocal:流程变量只在当前任务有效，如果任务完成就消失
		 * taskService.setVariableLocal(task.getId(), "请假天数", 10);
		taskService.setVariableLocal(task.getId(), "请假原因", "结婚");
		taskService.setVariableLocal(task.getId(), "开始时间", new Date());*/
		
		/*Map<String, Object> map = new HashMap<String, Object>();
		map.put("请假天数", 3);
		map.put("请假原因", "旅游去");
		map.put("请假开始时间", new Date());
		//当任务完成时来设置流程变量
		taskService.complete(task.getId(), map);*/
		
	}
	
	/**
	 * 获得流程变量
	 */
	@Test
	public void getVariablesIntask(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		//获得流程变量
		Integer days = (Integer) taskService.getVariable(task.getId(), "请假天数");
		String reason = (String) taskService.getVariable(task.getId(), "请假原因");
		Date startTime = (Date) taskService.getVariable(task.getId(), "开始时间");
		System.out.println(days);
		System.out.println(reason);
		System.out.println(startTime);
	}
	
	@Test
	public void getVariablesIntaskWithMap(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		//获得流程变量
		Map<String, Object> map = taskService.getVariables(task.getId());
		System.out.println(map);
	}
	
	@Test
	public void completeTask1(){
		String assignee = "employnee";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		taskService.complete(task.getId());
	}
	
	/**
	 * 通过任务服务接口在正在正在执行对象中放自定义的实体对象，实体类需要序列化而且还要生成UID（反序列化需要）
	 */
	@Test
	public void putVariablesIntaskObject(){
		String assignee = "employnee";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		
		LeaveBill leaveBill = new LeaveBill();
		leaveBill.setId(1);
		leaveBill.setStartTime(new Date());
		leaveBill.setDays("2");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("请假单对象", leaveBill);
		
		//当任务完成时来设置流程变量
		taskService.complete(task.getId(), map);
		
	}
	
	/**
	 * 通过任务来获取自定义实体的流程变量
	 */
	@Test
	public void getVariablesIntaskObjectWithMap(){
		String assignee = "manager";
		Task task = taskService.createTaskQuery()
				.processDefinitionKey("activit_leave")
				.taskAssignee(assignee)
				.singleResult();
		//获得流程变量
		Map<String, Object> map = taskService.getVariables(task.getId());
		LeaveBill leaveBill = (LeaveBill) map.get("请假单对象");
		System.out.println(leaveBill);
	}
	
	
	
}
