package com.example.myapplication.entity;

import java.io.Serializable;

public class AppInfo implements Serializable {
	private Integer appCode;
	private Integer appId;
	private String appName;

	private String appUpdateMessage;
	private String myAppInfo;
	private String url;
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Integer getAppCode() {
		return appCode;
	}
	public void setAppCode(Integer appCode) {
		this.appCode = appCode;
	}
	public String getAppUpdateMessage() {
		return appUpdateMessage;
	}
	public void setAppUpdateMessage(String appUpdateMessage) {
		this.appUpdateMessage = appUpdateMessage;
	}
	public String getMyAppInfo() {
		return myAppInfo;
	}
	public void setMyAppInfo(String myAppInfo) {
		this.myAppInfo = myAppInfo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public AppInfo(Integer appCode, Integer appId, String appName, String appUpdateMessage, String myAppInfo, String url) {
		this.appCode = appCode;
		this.appId = appId;
		this.appName = appName;
		this.appUpdateMessage = appUpdateMessage;
		this.myAppInfo = myAppInfo;
		this.url = url;
	}

	public AppInfo() {
	}
}
