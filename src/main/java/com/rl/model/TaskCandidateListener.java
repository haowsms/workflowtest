package com.rl.model;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskCandidateListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4018190610833176145L;

	@Override
	public void notify(DelegateTask delegateTask) {
		delegateTask.addCandidateUser("С��");
		delegateTask.addCandidateUser("С��");
		delegateTask.addCandidateUser("Сǿ");
		
		
	}

}
