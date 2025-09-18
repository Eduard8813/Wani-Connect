package com.eduard.registro.turismo.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
public class MailConfig {
    // Spring Boot auto-configures JavaMailSender based on application.properties
}