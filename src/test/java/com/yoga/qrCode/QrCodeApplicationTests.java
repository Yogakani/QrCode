package com.yoga.qrCode;

import com.yoga.qrCode.agent.MailService;
import com.yoga.qrCode.service.UserService;
import com.yoga.qrCode.utils.CommonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

@SpringBootTest
class QrCodeApplicationTests {

	@Autowired
	MailService mailService;
	@Autowired
	UserService userService;

	@Test
	void contextLoads() {
	}

	@Test
	public void testMail() throws MessagingException {
		String content = CommonUtils.tempPwdContent(userService.generateTempPass("8"), "Yoga");
		mailService.triggerMail(content, "yogakani32@gmail.com", "Temporary Password", "4323-4234");
	}

}
