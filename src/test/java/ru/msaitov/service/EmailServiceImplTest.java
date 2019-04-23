package ru.msaitov.service;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.msaitov.service.sendEMail.EmailServiceImpl;
import ru.msaitov.view.UserView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmailServiceImplTest {

    private JavaMailSender emailSender = mock(JavaMailSender.class);
    private Logger logger = mock(Logger.class);
    private EmailServiceImpl emailService = new EmailServiceImpl(emailSender, logger);
    private UserView userView;

    @Before
    public void setValue() {
        userView = new UserView();
        userView.setEmail("ddd@eee.ru");

    }

    @Test
    public void sendTest() {
        emailService.send(userView, "url", "token");
        verify(emailSender, times(1)).send((SimpleMailMessage) any());
    }


}
