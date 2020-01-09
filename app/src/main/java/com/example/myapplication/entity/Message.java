package com.example.myapplication.entity;

import java.io.Serializable;
import java.sql.Date;

public class Message  implements Serializable {
	private Integer id;
	private String message;
	private String messageInfo;
	private User userId;
	private Date datetiem;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	public Date getDatetiem() {
		return datetiem;
	}
	public void setDatetiem(Date datetiem) {
		this.datetiem = datetiem;
	}
	public String getMessageInfo() {
		return messageInfo;
	}
	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}

 }
