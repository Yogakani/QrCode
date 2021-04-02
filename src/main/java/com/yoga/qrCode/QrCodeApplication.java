package com.yoga.qrCode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
