package org.example.tp3client.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {

	@Bean
	public RestTemplate genereateUserRestTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
