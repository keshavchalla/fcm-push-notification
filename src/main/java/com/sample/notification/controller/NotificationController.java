package com.sample.notification.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.notification.service.PushNotificationsService;

@RestController
public class NotificationController {

	@Autowired
	PushNotificationsService pushNotificationsService;

	/**
	 * This service send a message to a topic and all the devices subscribing to this topic will receive the notification
	 * @return
	 * @throws JSONException
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> sendToTopic() throws JSONException, InterruptedException, ExecutionException {
		CompletableFuture<String> pushNotification = pushNotificationsService.sendToTopic();
		CompletableFuture.allOf(pushNotification).join();
		String firebaseResponse = pushNotification.get();
		return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
	}

	/**
	 * This service sends a notification to a single device using registrationToken
	 * @param registrationToken
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@RequestMapping(value = "/sendByToken", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> sendByRegistrationKToken(@RequestHeader("registrationToken") String registrationToken)
			throws InterruptedException, ExecutionException{
		CompletableFuture<String> pushNotification = pushNotificationsService.sendToDevice(registrationToken);
		CompletableFuture.allOf(pushNotification).join();
		String firebaseResponse = pushNotification.get();
		return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
	}
	
	/**
	 * @param userId
	 * @param registrationToken
	 * @return
	 */
	@RequestMapping(value = "/saveToken/{userId}/{registrationToken}", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> saveRegistrationToken(@RequestParam("userId") String userId, @RequestParam("registrationToken") String registrationToken){
		
		return null;
	}
	
	@RequestMapping(value = "/groupKey", method = RequestMethod.GET, produces = "application/json")
	public  ResponseEntity<String> getDeviceGroupKey(){
		String firebaseResponse = pushNotificationsService.getDeviceGroupKey();
		return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/retriveNotificationKey/{notificationKeyName}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> retriveDeviceGroupKey(@PathVariable("notificationKeyName") String notificationKeyName) {
		String firebaseResponse = pushNotificationsService.retriveDeviceGroupKey(notificationKeyName);
		return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/sendToGroup/{notificationKey}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> sendToGroup(@PathVariable("notificationKey") String notificationKey){
		String firebaseResponse = pushNotificationsService.sendMessageToMultiDevice(notificationKey);
		return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
	}
}
