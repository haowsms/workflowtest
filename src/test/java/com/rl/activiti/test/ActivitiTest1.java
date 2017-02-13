package com.rl.activiti.test;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Before;
import org.junit.Test;
/**
 * 流程定义管理
 *
 */
public class ActivitiTest1 {

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
	 * select * from act_re_deployment; -- 工作流部署表
	   select * from act_ge_bytearray; --  存储二进制相关的文件
	   select * from act_re_procdef;-- 流程定义的数据
	   
	   如果key相同，做二次部署会在原有的key的流程上做版本的累加
	 */
	@Test
	public void delpoyActiviti() {
		//创建部署对象
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//加载流程的配置文件和图片
		deploymentBuilder.addClasspathResource("diagrams/activit_test.bpmn")
						 .name("请假流程")
						 .category("请假")
						 .addClasspathResource("diagrams/activit_test.png");
		//部署流程
		deploymentBuilder.deploy();
	}
	
	/**
	 * 使用zip压缩包的方式部署
	 */
	@Test
	public void delpoyActiviti1() {
		//创建部署对象
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/activiti_test.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		//加载流程的配置文件和图片
		deploymentBuilder.addZipInputStream(zipInputStream)
		.name("请假流程")
		.category("请假");
		//部署流程
		deploymentBuilder.deploy();
	}
	
	/**
	 * 删除流程部署
	 */
	@Test
	public void deleteActivitiDeploy() {
		
		String deploymentId = "201";
		//如果流程已经启动删除会报错
		//repositoryService.deleteDeployment(deploymentId);
		
		//如果第二个参数是true级联删除，即使有正在运行的数据也会被删除，如果是false非级联删除如果有数据在运行，会报错
		repositoryService.deleteDeployment(deploymentId, true);
	}
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("activit_leave");
	}
	
	/**
	 * 根据流程定义的ID来查询流程定义对象
	 */
	@Test
	public void queryProcessDefinitionById() {
		String processDefinitionId = "activit_leave:2:2504";
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()//创建流程定义查询对象
						.processDefinitionId(processDefinitionId)//根据id来查询
						.singleResult();//得到唯一结果
		System.out.println("流程定义的Id："+processDefinition.getId());
		System.out.println("流程定义的key："+processDefinition.getKey());
		System.out.println("流程定义的名字："+processDefinition.getName());
		System.out.println("流程定义的资源文件名字："+processDefinition.getResourceName());
	}
	
	/**
	 * 根据Key来查询流程定义对象
	 */
	@Test
	public void queryProcessDefinitionByKey() {
		String processDefinitionKey = "activit_leave";
		
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey)
				.list();
		for(ProcessDefinition processDefinition : processDefinitionList){
			System.out.println("流程定义的Id："+processDefinition.getId());
			System.out.println("流程定义的key："+processDefinition.getKey());
			System.out.println("流程定义的名字："+processDefinition.getName());
			System.out.println("流程定义的版本："+processDefinition.getVersion());
			System.out.println("流程定义的资源文件名字："+processDefinition.getResourceName());
		}
		
	}
	

}
