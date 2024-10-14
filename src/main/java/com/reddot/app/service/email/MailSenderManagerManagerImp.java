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

import java.util.Set;

@Setter
@Service
public class MailSenderManagerManagerImp implements MailSenderManager {

    private final JavaMailSender mailSender;

    public MailSenderManagerManagerImp(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress("Reddot"));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(body);
        };

        this.send(preparator);
    }

    @Override
    public void sendEmails(Set<String> emails, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("Reddot");
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        for (String email : emails) {
            simpleMailMessage.setTo(email);
            this.send(simpleMailMessage);
        }
    }

    private void send(SimpleMailMessage simpleMailMessage) throws MailSendException {
        try {
            mailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new MailSendException("Failed to send email", e);
        }
    }

    private void send(MimeMessagePreparator preparator) {
        try {
            mailSender.send(preparator);
        } catch (MailException e) {
            throw new MailSendException("Failed to send email", e);
        }
    }
}
