package com.sample.notification.model;

import java.util.Map;

public class NotificationMessage {

	private String notificationTitle;
	private String notificationBody;
	private Map<String,String> notificationData;
	/**
	 * @return the notificationTitle
	 */
	public String getNotificationTitle() {
		return notificationTitle;
	}
	/**
	 * @param notificationTitle the notificationTitle to set
	 */
	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}
	/**
	 * @return the notificationBody
	 */
	public String getNotificationBody() {
		return notificationBody;
	}
	/**
	 * @param notificationBody the notificationBody to set
	 */
	public void setNotificationBody(String notificationBody) {
		this.notificationBody = notificationBody;
	}
	/**
	 * @return the notificationData
	 */
	public Map<String, String> getNotificationData() {
		return notificationData;
	}
	/**
	 * @param notificationData the notificationData to set
	 */
	public void setNotificationData(Map<String, String> notificationData) {
		this.notificationData = notificationData;
	}
}
