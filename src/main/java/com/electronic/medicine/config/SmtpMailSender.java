package com.electronic.medicine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class SmtpMailSender {

    @Value("${spring.mail.host}")
    private String host;
    //TODO ПЕРЕМЕННЫЕ КОНФИГИ

    private final String username = System.getenv("MAIL_USERNAME");

    private final String password = System.getenv("MAIL_PASSWORD");

//    private final String username = System.getenv("SPRING_MAIL_USERNAME");
//
//    private final String password = System.getenv("SPRING_MAIL_PASSWORD");


    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${mail.debug}")
    private String debug;



    @Bean
    public JavaMailSender createMailSender () {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        Properties javaMailProperties = javaMailSender.getJavaMailProperties();
        javaMailProperties.setProperty("mail.transport.protocol", protocol);
        javaMailProperties.setProperty("mail.debug", debug);
        return javaMailSender;
    }
}
