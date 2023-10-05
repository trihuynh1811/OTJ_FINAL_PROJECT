package com.example.FAMS.services;

import com.example.FAMS.dtos.request.LoginRequest;
import com.example.FAMS.repositories.TokenDAO;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.service_implementors.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
    @Mock
    private UserDAO userDAO;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTService jwtService;
    @Mock
    private TokenDAO tokenDAO;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void User_Login_returnLoginResponse() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin@gmail.com")
                .password("1")
                .build();

//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn()
    }


}
