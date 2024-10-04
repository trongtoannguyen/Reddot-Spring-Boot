package com.reddot.app.service.email;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.Setter;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Setter
@Service
public class MailSenderManagerManagerImp implements MailSenderManager {

    private final JavaMailSender mailSender;

    public MailSenderManagerManagerImp(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailSendException {
        try {
            mailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new MailSendException("Failed to send email", e);
        }
    }

    @Override
    public void send(MimeMessagePreparator preparator) {
        try {
            mailSender.send(preparator);
        } catch (MailException e) {
            throw new MailSendException("Failed to send email", e);
        }
    }

    // Helper method to send email
    @Override

    public void sendEmail(String to, String subject, String body) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress("Reddot"));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(body);
        };

        try {
            this.send(preparator);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

}
