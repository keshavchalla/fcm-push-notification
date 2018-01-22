package com.sample.notification.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
	public CompletableFuture<String> sendToTopic() {
		JSONObject body = new JSONObject();
		body.put("to", "/topics/" + firebaseServerTopic);
		body.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", "FCM Notification");
		notification.put("body", "Happy Message!");
		
		JSONObject data = new JSONObject();
		data.put("Key-1", "FCM Data 1");
		data.put("Key-2", "FCM Data 2");

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
	public CompletableFuture<String> sendToDevice(String registrationToken) {
		JSONObject body = new JSONObject();
		body.put("to", registrationToken);
		body.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", "Device Notification");
		notification.put("body", "Happy Message For a single device !");
		body.put("notification", notification);
		HttpEntity<String> request = new HttpEntity<>(body.toString());
		String firebaseResponse = restTemplate.postForObject(firebaseServerUrl, request, String.class);
		return CompletableFuture.completedFuture(firebaseResponse);
	}
	
	public String getDeviceGroupKey() {
		List<String> tokens = new ArrayList<String>();
		tokens.add("cGZIkFFC-Po:APA91bFICUxedtwl528W8otvw0bzw_yB7I-aS-jLLTlLKGNO67G5a1yeLO29_hgGAcJl4I6V_eSEmBW8ukH7R_XXwdYlKq9bZT9z0TFYPPyLNMiF7UM6fdrPMVUxe_zMp52uIirjzi1P");
		tokens.add("dxXO4Jgoutc:APA91bHLkYk4wDtevu5tPMb8tNKQ96nlf0qOHSXNi4Q-SMNAs-IG6lju2Xhvp6vx6KkcWdDaa7LoicxdopcKDSwxVEtBmz7zT1pDPblV8rFGk7aLrC9gUfpgREF46Al-jGROI-wvWY4m");
		JSONObject body = new JSONObject();
		body.put("operation", "create");
		body.put("notification_key_name", "ke-token");
		body.put("registration_ids", tokens);
		
		HttpEntity<String> request = new HttpEntity<>(body.toString());
		String firebaseResponse = restTemplate.postForObject(gcmServerUrl, request, String.class);
		return firebaseResponse;
	}
	
	public String retriveDeviceGroupKey(String notificationKeyName) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(gcmServerUrl)
				.queryParam("notification_key_name", notificationKeyName);
		return restTemplate.getForObject(builder.build().encode().toUri(), String.class);
	}
	
	public String sendMessageToMultiDevice(String notificationKey) {
		JSONObject body = new JSONObject();
		body.put("to", notificationKey);
		
		JSONObject notification = new JSONObject();
		notification.put("title", "Notification to MultipleDevice");
		notification.put("body", "Happy Message to multiple devices");
		body.put("notification", notification);
		
		JSONObject data = new JSONObject();
		data.put("hello", "This is a Firebase Cloud Messaging Device Group Message!");
		body.put("data", data);
		HttpEntity<String> request = new HttpEntity<>(body.toString());
		String firebaseResponse = restTemplate.postForObject(firebaseServerUrl, request, String.class);
		return firebaseResponse;
	}
}
