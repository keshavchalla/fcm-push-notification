package com.sample.notification.model;

import java.util.List;

public class DeviceGroup {

	private String notificationKeyName;
	private List<String> deviceRegistrationTokens;
	
	/**
	 * @return the notificationKeyName
	 */
	public String getNotificationKeyName() {
		return notificationKeyName;
	}
	/**
	 * @param notificationKeyName the notificationKeyName to set
	 */
	public void setNotificationKeyName(String notificationKeyName) {
		this.notificationKeyName = notificationKeyName;
	}
	/**
	 * @return the deviceRegistrationTokens
	 */
	public List<String> getDeviceRegistrationTokens() {
		return deviceRegistrationTokens;
	}
	/**
	 * @param deviceRegistrationTokens the deviceRegistrationTokens to set
	 */
	public void setDeviceRegistrationTokens(List<String> deviceRegistrationTokens) {
		this.deviceRegistrationTokens = deviceRegistrationTokens;
	}
}
