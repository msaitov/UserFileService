package ru.msaitov.service;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import ru.msaitov.controller.exception.UserAlreadyExistException;
import ru.msaitov.repository.UserRepository;
import ru.msaitov.repository.VerificationTokenRepository;
import ru.msaitov.service.sendEMail.EmailService;
import ru.msaitov.service.verificationUser.UserServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserServiceImplTest extends MockInit {

    private EmailService emailService = mock(EmailService.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private VerificationTokenRepository tokenRepository = mock(VerificationTokenRepository.class);
    private UserServiceImpl userService = new UserServiceImpl(emailService, userRepository, tokenRepository, mapper, logger);
    private UserServiceImpl userServiceSpy = Mockito.spy(userService);


    @Test
    public void registerNewUserAccountTest() throws UserAlreadyExistException {
        when(mapper.map(userView)).thenReturn(userEntity);
        userServiceSpy.registerNewUserAccount(userView, "url");
    }

    @Test
    public void loadUserByUsernameTest() {
        when(userRepository.findByEmail("username")).thenReturn(userEntity);
        when(mapper.map(userEntity)).thenReturn(userView);
        UserDetails userViewMethod = userServiceSpy.loadUserByUsername("username");
        assertEquals(userViewMethod.getUsername(), userView.getEmail());
    }


}
