package com.reddot.app.service.email;

import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessagePreparator;

public interface MailSenderManager {
    void send(SimpleMailMessage simpleMailMessage) throws MailSendException;

    void send(MimeMessagePreparator preparator);

    void sendEmail(String email, String subject, String body);
}

