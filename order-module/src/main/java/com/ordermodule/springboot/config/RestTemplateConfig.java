package com.ordermodule.springboot.config;

import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
				.setConnectTimeout(Duration.ofSeconds(3))
				.setReadTimeout(Duration.ofSeconds(3))
				.additionalInterceptors(clientHttpRequestInterceptor())
				.build();
	}

	public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
		return (request, body, execution) -> {
			RetryTemplate retryTemplate = new RetryTemplate();
			FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
			fixedBackOffPolicy.setBackOffPeriod(5000L);
			retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

			SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3, retryableException());
			retryTemplate.setRetryPolicy(retryPolicy);

				return retryTemplate.execute(
						context -> {
							log.info("restTemplate try");
							return execution.execute(request, body);
						}
				);
		};
	}

	private Map<Class<? extends Throwable>, Boolean> retryableException() {
		Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
		retryableExceptions.put(ResourceAccessException.class, true);
		retryableExceptions.put(SocketTimeoutException.class, true);
		retryableExceptions.put(HttpServerErrorException.ServiceUnavailable.class, true);
		retryableExceptions.put(HttpServerErrorException.BadGateway.class, true);
		retryableExceptions.put(HttpServerErrorException.GatewayTimeout.class, true);
		retryableExceptions.put(HttpClientErrorException.TooManyRequests.class, true);
		return retryableExceptions;
	}


}
