package ru.msaitov.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.msaitov.model.VerificationToken;
import ru.msaitov.repository.UserRepository;
import ru.msaitov.repository.VerificationTokenRepository;
import ru.msaitov.service.verificationToken.TokenState;
import ru.msaitov.service.verificationToken.VerificationTokenServiceImpl;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VerificationTokenServiceImplTest extends MockInit {

    private VerificationTokenRepository tokenRepository = mock(VerificationTokenRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private VerificationTokenServiceImpl verificationTokenService = new VerificationTokenServiceImpl(tokenRepository, userRepository, logger);
    private VerificationTokenServiceImpl verificationTokenServiceSpy = Mockito.spy(verificationTokenService);
    private String testToken;

    @Before
    public void setValueToken() {
        testToken = "qwerty";
    }


    @Test
    public void validateVerificationTokenTestInvalid() {
        TokenState tokenState = verificationTokenServiceSpy.validateVerificationToken(testToken);
        TokenState tokenStateExpect = TokenState.TOKEN_INVALID;
        assertEquals(tokenState, tokenStateExpect);
    }

    @Test
    public void validateVerificationTokenTestExpired() {
        VerificationToken verificationToken = new VerificationToken();
        Date date = calculateExpiryDate(60 * 24 * -1);

        verificationToken.setExpiryDate(date);
        when(tokenRepository.findByToken(testToken)).thenReturn(verificationToken);
        TokenState tokenState = verificationTokenServiceSpy.validateVerificationToken(testToken);
        TokenState tokenStateExpect = TokenState.TOKEN_EXPIRED;
        assertEquals(tokenState, tokenStateExpect);
    }

    @Test
    public void validateVerificationTokenTestValid() {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUserEntity(userEntity);
        Date date = calculateExpiryDate(60 * 24 * 1);

        verificationToken.setExpiryDate(date);
        when(tokenRepository.findByToken(testToken)).thenReturn(verificationToken);
        TokenState tokenState = verificationTokenServiceSpy.validateVerificationToken(testToken);
        TokenState tokenStateExpect = TokenState.TOKEN_VALID;
        assertEquals(tokenState, tokenStateExpect);
    }


    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

}
