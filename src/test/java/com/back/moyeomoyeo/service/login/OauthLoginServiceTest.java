package com.back.moyeomoyeo.service.login;

import com.back.moyeomoyeo.service.session.SessionManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OauthLoginServiceTest {
    @Test
    void Oauth_성공_회원() {
        OauthLoginService loginService = mock(OauthLoginService.class);
        SessionManager sessionManager = mock(SessionManager.class);

        when(loginService.isAccount(anyString())).thenReturn(Boolean.TRUE);

        if (loginService.isAccount("test") == Boolean.TRUE)
             sessionManager.createConnection();

        verify(sessionManager, times(1)).createConnection();
    }

    @Test
    void Oauth_성공_비회원() {
        OauthLoginService loginService = mock(OauthLoginService.class);
        SessionManager sessionManager = mock(SessionManager.class);

        when(loginService.isAccount(anyString())).thenReturn(Boolean.FALSE);

        if (loginService.isAccount("test") == Boolean.FALSE) {
            loginService.doAccount();
            sessionManager.createConnection();
        }
        verify(loginService, times(1)).doAccount();
        verify(sessionManager, times(1)).createConnection();
    }
}