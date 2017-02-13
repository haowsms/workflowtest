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
 * ���̶������
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
	 * ��������
	 * select * from act_re_deployment; -- �����������
	   select * from act_ge_bytearray; --  �洢��������ص��ļ�
	   select * from act_re_procdef;-- ���̶��������
	   
	   ���key��ͬ�������β������ԭ�е�key�����������汾���ۼ�
	 */
	@Test
	public void delpoyActiviti() {
		//�����������
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//�������̵������ļ���ͼƬ
		deploymentBuilder.addClasspathResource("diagrams/activit_test.bpmn")
						 .name("�������")
						 .category("���")
						 .addClasspathResource("diagrams/activit_test.png");
		//��������
		deploymentBuilder.deploy();
	}
	
	/**
	 * ʹ��zipѹ�����ķ�ʽ����
	 */
	@Test
	public void delpoyActiviti1() {
		//�����������
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/activiti_test.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		//�������̵������ļ���ͼƬ
		deploymentBuilder.addZipInputStream(zipInputStream)
		.name("�������")
		.category("���");
		//��������
		deploymentBuilder.deploy();
	}
	
	/**
	 * ɾ�����̲���
	 */
	@Test
	public void deleteActivitiDeploy() {
		
		String deploymentId = "201";
		//��������Ѿ�����ɾ���ᱨ��
		//repositoryService.deleteDeployment(deploymentId);
		
		//����ڶ���������true����ɾ������ʹ���������е�����Ҳ�ᱻɾ���������false�Ǽ���ɾ����������������У��ᱨ��
		repositoryService.deleteDeployment(deploymentId, true);
	}
	
	@Test
	public void startProcess() {
		runtimeService.startProcessInstanceByKey("activit_leave");
	}
	
	/**
	 * �������̶����ID����ѯ���̶������
	 */
	@Test
	public void queryProcessDefinitionById() {
		String processDefinitionId = "activit_leave:2:2504";
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()//�������̶����ѯ����
						.processDefinitionId(processDefinitionId)//����id����ѯ
						.singleResult();//�õ�Ψһ���
		System.out.println("���̶����Id��"+processDefinition.getId());
		System.out.println("���̶����key��"+processDefinition.getKey());
		System.out.println("���̶�������֣�"+processDefinition.getName());
		System.out.println("���̶������Դ�ļ����֣�"+processDefinition.getResourceName());
	}
	
	/**
	 * ����Key����ѯ���̶������
	 */
	@Test
	public void queryProcessDefinitionByKey() {
		String processDefinitionKey = "activit_leave";
		
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey)
				.list();
		for(ProcessDefinition processDefinition : processDefinitionList){
			System.out.println("���̶����Id��"+processDefinition.getId());
			System.out.println("���̶����key��"+processDefinition.getKey());
			System.out.println("���̶�������֣�"+processDefinition.getName());
			System.out.println("���̶���İ汾��"+processDefinition.getVersion());
			System.out.println("���̶������Դ�ļ����֣�"+processDefinition.getResourceName());
		}
		
	}
	

}
