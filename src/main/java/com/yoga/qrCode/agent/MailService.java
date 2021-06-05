package com.yoga.qrCode.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void triggerMail(String content, String to, String subject, String requestId) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            log.info("{} Sending mail to user : {}", requestId, to);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("{} Exception occurred while sending mail : {}", requestId, e.getMessage());
            throw e;
        }
    }

}
