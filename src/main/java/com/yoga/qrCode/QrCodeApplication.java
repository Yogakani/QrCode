package com.yoga.qrCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import static com.yoga.qrCode.utils.CommonUtils.getHttpStatusCode;

@SpringBootApplication
@RequestMapping(value = "/")
public class QrCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrCodeApplication.class, args);
	}

	@GetMapping(value = "/")
	public ResponseEntity<String> appMsg() {
		return new ResponseEntity<>("Welcome to App", getHttpStatusCode(true));
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ObjectMapper getMapper() {
		return new ObjectMapper();
	}

}
