package com.rl.model;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MyTaskListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6220986694761944389L;

	@Override
	public void notify(DelegateTask delegateTask) {
		//��õ�ǰ��DefinitionKey��manager_audit��
		String taskKey = delegateTask.getTaskDefinitionKey();
		//�������������������������
		if(taskKey.equals(Constants.MANAGER_AUDIT)){
			delegateTask.setAssignee("����");
		}
		//����ϰ�������������������
		if(taskKey.equals(Constants.BOSS_AUDIT)){
			delegateTask.setAssignee("����");
		}
		
	}

}
