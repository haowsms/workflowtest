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
		//获得当前的DefinitionKey（manager_audit）
		String taskKey = delegateTask.getTaskDefinitionKey();
		//如果经理审批把任务分配给张三
		if(taskKey.equals(Constants.MANAGER_AUDIT)){
			delegateTask.setAssignee("王五");
		}
		//如果老板审批把任务分配给李四
		if(taskKey.equals(Constants.BOSS_AUDIT)){
			delegateTask.setAssignee("赵六");
		}
		
	}

}
