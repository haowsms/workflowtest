package com.rl.model;

import java.io.Serializable;
import java.util.Date;

public class LeaveBill implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7073892744812370984L;

	Integer id;
	
	String days;
	
	Date startTime;
	
	String reason;
	
	String userId;
	
	
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	

}
