package ru.msaitov.service.sendEMail;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msaitov.view.UserView;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    private static Logger logger;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, Logger logger) {
        this.emailSender = emailSender;
        EmailServiceImpl.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(UserView user, String url, String token) {
        logger.info("[SERVICE] send");
        final String subject = "Registration Confirmation";
        final String confirmationUrl = url + "/registrationConfirm.html?token=" + token;
        String message = "Click on the link to confirm your email.";
        message = message + " \r\n" + confirmationUrl;
        sendSimpleMessage(user.getEmail(), subject, message);
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        logger.info("[SERVICE] sendSimpleMessage");
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);
        emailSender.send(email);
    }
}
