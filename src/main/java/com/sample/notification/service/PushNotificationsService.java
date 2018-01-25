package com.sample.notification.service;

import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sample.notification.model.DeviceGroup;
import com.sample.notification.model.NotificationMessage;

@Service
public class PushNotificationsService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${firebase.server.url}")
	private String firebaseServerUrl;
	@Value("${gcm.server.url}")
	private String gcmServerUrl;
	@Value("${firebase.server.topic}")
	private String firebaseServerTopic;
	
	
	/**
	 * @return
	 */
	@Async
	public CompletableFuture<String> sendToTopic(String topicName,NotificationMessage notificationMessage) {
		JSONObject body = new JSONObject();
		body.put("to", "/topics/" + topicName);
		body.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", notificationMessage.getNotificationTitle());
		notification.put("body", notificationMessage.getNotificationBody());
		
		JSONObject data = new JSONObject();
		notificationMessage.getNotificationData().forEach((k,v) -> {
			data.put(k, v);
		});

		body.put("notification", notification);
		body.put("data", data);
		
		HttpEntity<String> request = new HttpEntity<>(body.toString());
		String firebaseResponse = restTemplate.postForObject(firebaseServerUrl, request, String.class);
		return CompletableFuture.completedFuture(firebaseResponse);
	}
	
	/**
	 * @param registrationToken
	 * @return
	 */
	@Async
	public CompletableFuture<String> sendToDevice(String registrationToken, NotificationMessage notificationMessage) {
		JSONObject body = new JSONObject();
		body.put("to", registrationToken);
		body.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", notificationMessage.getNotificationTitle());
		notification.put("body", notificationMessage.getNotificationBody());
		body.put("notification", notification);
		HttpEntity<String> request = new HttpEntity<>(body.toString());
		String firebaseResponse = restTemplate.postForObject(firebaseServerUrl, request, String.class);
		return CompletableFuture.completedFuture(firebaseResponse);
	}
	
	public String getDeviceGroupKey(DeviceGroup deviceGroup) {
		/*List<String> tokens = new ArrayList<String>();
		tokens.add("cGZIkFFC-Po:APA91bFICUxedtwl528W8otvw0bzw_yB7I-aS-jLLTlLKGNO67G5a1yeLO29_hgGAcJl4I6V_eSEmBW8ukH7R_XXwdYlKq9bZT9z0TFYPPyLNMiF7UM6fdrPMVUxe_zMp52uIirjzi1P");
		tokens.add("dxXO4Jgoutc:APA91bHLkYk4wDtevu5tPMb8tNKQ96nlf0qOHSXNi4Q-SMNAs-IG6lju2Xhvp6vx6KkcWdDaa7LoicxdopcKDSwxVEtBmz7zT1pDPblV8rFGk7aLrC9gUfpgREF46Al-jGROI-wvWY4m");*/
		JSONObject body = new JSONObject();
		body.put("operation", "create");
		body.put("notification_key_name", deviceGroup.getNotificationKeyName());
		body.put("registration_ids", deviceGroup.getDeviceRegistrationTokens());
		
		HttpEntity<String> request = new HttpEntity<>(body.toString());
		String firebaseResponse = restTemplate.postForObject(gcmServerUrl, request, String.class);
		return firebaseResponse;
	}
	
	public String retriveDeviceGroupKey(String notificationKeyName) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(gcmServerUrl)
				.queryParam("notification_key_name", notificationKeyName);
		return restTemplate.getForObject(builder.build().encode().toUri(), String.class);
	}
	
	public String sendMessageToMultiDevice(String notificationKey, NotificationMessage notificationMessage) {
		JSONObject body = new JSONObject();
		body.put("to", notificationKey);
		
		JSONObject notification = new JSONObject();
		notification.put("title", notificationMessage.getNotificationTitle());
		notification.put("body", notificationMessage.getNotificationBody());
		body.put("notification", notification);
		
		JSONObject data = new JSONObject();
		notificationMessage.getNotificationData().forEach((k,v) -> {
			data.put(k, v);
		});
		//data.put("hello", "This is a Firebase Cloud Messaging Device Group Message!");
		body.put("data", data);
		HttpEntity<String> request = new HttpEntity<>(body.toString());
		String firebaseResponse = restTemplate.postForObject(firebaseServerUrl, request, String.class);
		return firebaseResponse;
	}
}
