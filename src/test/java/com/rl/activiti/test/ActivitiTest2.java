package com.rl.activiti.test;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.junit.Before;
import org.junit.Test;
/**
 * 流程实例管理
 *
 */
public class ActivitiTest2 {

	RepositoryService repositoryService;
	
	RuntimeService runtimeService;
	
	@Before
	public void setUp() throws Exception {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
	}

	/**
	 * 部署流程
	 *  select * from act_ru_execution; -- 正在运行的流程对象
		select * from act_hi_procinst; -- 历史流程实例表
		select * from act_ru_task; -- 任务表
		select * from act_hi_taskinst; -- 历史任务实例表
		select * from act_hi_actinst; -- 历史所有经过的活动
	   如果key相同，做二次部署会在原有的key的流程上做版本的累加
	 */
	@Test
	public void startProcess() {
		//启动流程，根据流程定义的key，如果相同的流程定义有多个，那么启动最高版本，如果想启用其他版本可以根据id来启动（act_re_procdef表中的id）
		ProcessInstance processInstance = runtimeService.startProcessInstanceById("activit_leave:1:4");
		System.out.println("流程实例ID："+processInstance.getId());
		System.out.println("正在活动的节点ID："+processInstance.getActivityId());//正在活动的流程节点
		System.out.println("流程定义ID："+processInstance.getProcessDefinitionId());//正在活动的流程节点
		
	}
	
	@Test
	public void queryProcessInstance(){
		String processDefinitionKey = "activit_leave";
		
		//创建流程实例查询对象
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		
		/*List<ProcessInstance> processInstanceList = processInstanceQuery
				.processDefinitionKey(processDefinitionKey)//根据流程定义的key（activit_leave）来查询
				.orderByProcessDefinitionKey()//根据流程定义的key排序
				.desc()//倒序排序
				.list();
		
		
		for(ProcessInstance processInstance : processInstanceList){
			System.out.println("流程实例ID："+processInstance.getId());
			System.out.println("正在活动的节点ID："+processInstance.getActivityId());//正在活动的流程节点
			System.out.println("流程定义ID："+processInstance.getProcessDefinitionId());//正在活动的流程节点
		}*/
		
		
		ProcessInstance processInstance1 = processInstanceQuery
				.processDefinitionKey(processDefinitionKey).singleResult();//如果能确定数据库查询的结果只有一条可以用singleResult，如果多个就会报错
				
		System.out.println("流程实例ID："+processInstance1.getId());
		System.out.println("正在活动的节点ID："+processInstance1.getActivityId());//正在活动的流程节点
		System.out.println("流程定义ID："+processInstance1.getProcessDefinitionId());//正在活动的流程节点
	}
	
	
	
	
	
	
	
	

}
