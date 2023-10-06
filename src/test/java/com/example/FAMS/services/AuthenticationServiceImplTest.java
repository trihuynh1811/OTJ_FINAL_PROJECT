package com.example.FAMS.services;

import com.example.FAMS.dto.requests.LoginRequest;
import com.example.FAMS.dto.responses.LoginResponse;

import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.TokenDAO;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.service_implementors.AuthenticationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
        int userId = 123;
        Role role = Role.USER;
        // Create a mock User object
        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setRole(Role.USER);

        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin@gmail.com")
                .password("1")
                .build();
        User user = User.builder()
                .email("admin@gmail.com")
                .build();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication customAuthentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(customAuthentication);
        when(userDAO.findByEmail(Mockito.any())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(user)).thenReturn("mockedToken");

        var token = jwtService.generateToken(user);
        var person = userDAO.findByEmail(loginRequest.getEmail())
                .orElseThrow();


        LoginResponse response = authenticationService.login(loginRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo("Successful");
        // Mock the behavior of jwtService.generateToken

    }


}
