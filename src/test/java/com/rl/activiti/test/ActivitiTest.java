package com.rl.activiti.test;

import static org.junit.Assert.*;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Before;
import org.junit.Test;
/**
 * ���������ʼ��
 *
 */
public class ActivitiTest {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * ʹ��java�����ʼ������
	 */
	@Test
	public void initContextWithCode() {
		//���������������ö���
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		//�������ݿ������
		processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activiti0228");
		processEngineConfiguration.setJdbcUsername("root");
		processEngineConfiguration.setJdbcPassword("root");
		//���ݿ�Ľ������
		/*DB_SCHEMA_UPDATE_FALSE = "false";������ݿ�����û�б��Զ�Ϊ���ǽ���activiti��23�ű� ����оͻ����쳣
		  "create-drop";���������汻����ʱ���Զ�����,�����������رջ�ɾ����
		 "true"������ݿ�����û�б���Զ���������б�Ͳ����������activiti��sql�ű��и��£���ô���������ٴν���ʱ��������ݿ�ı�Ҫ�õģ�
		 */
		processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		//���������������
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println(processEngine);
	}
	/**
	 * ͨ�������ļ�����ʼ�������������
	 */
	@Test
	public void initContextWithXMLConfig() {
		//��ȡ�����ļ�����������������ö���
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		//���������������
		ProcessEngine pe = processEngineConfiguration.buildProcessEngine();
		System.out.println(pe);
	}

}
