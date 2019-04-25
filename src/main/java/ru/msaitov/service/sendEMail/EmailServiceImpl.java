package ru.msaitov.service.sendEMail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.msaitov.view.UserView;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    private static Logger logger = LogManager.getFormatterLogger(EmailServiceImpl.class);

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(UserView user, String url, String token) {
        final String subject = "Registration Confirmation";
        final String confirmationUrl = url + "/registrationConfirm.html?token=" + token;
        logger.info("Для аккаунта: {}, выслать на email ссылку для активации: {}", user.getEmail(), confirmationUrl);
        String message = "Click on the link to confirm your email.";
        message = message + " \r\n" + confirmationUrl;
        sendSimpleMessage(user.getEmail(), subject, message);
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        logger.info("Выслать сообщение на email: {}, с темой: {}, текстом: {}", to, subject, text);
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);
        emailSender.send(email);
    }
}
