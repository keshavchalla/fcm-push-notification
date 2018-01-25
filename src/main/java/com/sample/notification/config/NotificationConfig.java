package com.sample.notification.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Predicates;
import com.sample.notification.util.HeaderRequestInterceptor;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class NotificationConfig {

	@Value("${firebase.server.key}")
	private String firebaseServerKey;
	@Value("${firebase.server.senderId}")
	private String senderId;
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + firebaseServerKey));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		interceptors.add(new HeaderRequestInterceptor("project_id", senderId));
		restTemplate.setInterceptors(interceptors);
		return restTemplate;
	}
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(Predicates.not(PathSelectors.regex("/error")))                          
          .build();                                           
    }
	
	/*private static String getAccessToken() throws IOException {
		  GoogleCredential googleCredential = GoogleCredential
		      .fromStream(new FileInputStream("service-account.json"))
		      .createScoped(Arrays.asList(SCOPES));
		  googleCredential.refreshToken();
		  return googleCredential.getAccessToken();
		}*/
	
	
}
