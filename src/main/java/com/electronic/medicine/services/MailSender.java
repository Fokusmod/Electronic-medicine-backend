package com.electronic.medicine.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailSender {

    private final JavaMailSender javaMailSender;

    //TODO ПЕРЕМЕННЫЕ КОНФИГИ
//    private String username = System.getenv("SPRING_MAIL_USERNAME");
    private String username = System.getenv("MAIL_USERNAME");

    public void sendActivateCodeMessage(String emailTo, String subject, String message) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(username, "Electronic Medicine");
        messageHelper.setTo(emailTo);
        messageHelper.setSubject(subject);
        messageHelper.setText(message , true);
        javaMailSender.send(mimeMessage);
    }





}
