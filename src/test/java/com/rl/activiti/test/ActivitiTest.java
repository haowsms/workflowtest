package com.rl.activiti.test;

import static org.junit.Assert.*;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Before;
import org.junit.Test;
/**
 * 流程引擎初始化
 *
 */
public class ActivitiTest {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * 使用java代码初始化环境
	 */
	@Test
	public void initContextWithCode() {
		//创建流程引擎配置对象
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		//设置数据库的驱动
		processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activiti0228");
		processEngineConfiguration.setJdbcUsername("root");
		processEngineConfiguration.setJdbcPassword("root");
		//数据库的建表策略
		/*DB_SCHEMA_UPDATE_FALSE = "false";如果数据库里面没有表，自动为我们建立activiti的23张表， 如果有就会抛异常
		  "create-drop";当流程引擎被创建时会自动建表,如果流程引擎关闭会删掉表
		 "true"如果数据库里面没有表会自动建表，如果有表就不建立，如果activiti的sql脚本有更新，那么流程引擎再次建立时会更新数据库的表（要用的）
		 */
		processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		//创建流程引擎对象
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println(processEngine);
	}
	/**
	 * 通过配置文件来初始化流程引擎对象
	 */
	@Test
	public void initContextWithXMLConfig() {
		//读取配置文件创建流程引擎的配置对象
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		//创建流程引擎对象
		ProcessEngine pe = processEngineConfiguration.buildProcessEngine();
		System.out.println(pe);
	}

}
