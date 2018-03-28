package com.fairanb;

import com.fairanb.config.SwaggerConfig;
import com.fairanb.utils.UserContextFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;


/**
 *@longmt99
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableBinding(Source.class)
@EnableDiscoveryClient
@EnableResourceServer
@Import(SwaggerConfig.class)
public class MerchantRestAPI{

	private static final Logger log = LoggerFactory.getLogger(MerchantRestAPI.class);

	@Value("${spring.datasource.url}")
	String datasource;

	@Value("${spring.profiles.active}")
	String env;
	
	@PostConstruct
	public void init() {
		log.info("RUN datasource [" +env+"] " + datasource );
	}

	public static void main(String[] args) {
		log.info("Start Application Context REST");
		SpringApplication.run(MerchantRestAPI.class, args);
	}

	@Bean
	public Filter userContextFilter() {
		UserContextFilter userContextFilter = new UserContextFilter();
		return userContextFilter;
	}
}
