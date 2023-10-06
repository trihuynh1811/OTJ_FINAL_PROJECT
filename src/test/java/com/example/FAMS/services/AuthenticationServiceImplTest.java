package com.example.FAMS.services;

import com.example.FAMS.dto.requests.CreateRequest;
import com.example.FAMS.dto.requests.LoginRequest;
import com.example.FAMS.dto.responses.CreateResponse;
import com.example.FAMS.dto.responses.LoginResponse;

import com.example.FAMS.enums.Role;
import com.example.FAMS.models.EmailDetails;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
  private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
  @Mock private UserDAO userDAO;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private JWTService jwtService;
  @Mock private TokenDAO tokenDAO;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private EmailService emailService;
  @InjectMocks private AuthenticationServiceImpl authenticationService;

  @Test
  void User_Login_returnLoginResponse() {
    int userId = 123;
    Role role = Role.USER;
    // Create a mock User object
    User mockUser = new User();
    mockUser.setUserId(userId);
    mockUser.setRole(Role.USER);

    LoginRequest loginRequest =
        LoginRequest.builder().email("admin@gmail.com").password("1").build();
    User user = User.builder().email("admin@gmail.com").build();
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    Authentication customAuthentication =
        new UsernamePasswordAuthenticationToken(user, null, authorities);

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(customAuthentication);
    when(userDAO.findByEmail(Mockito.any())).thenReturn(Optional.of(mockUser));
    when(jwtService.generateToken(user)).thenReturn("mockedToken");

    var token = jwtService.generateToken(user);
    var person = userDAO.findByEmail(loginRequest.getEmail()).orElseThrow();

    LoginResponse response = authenticationService.login(loginRequest);

    Assertions.assertThat(response).isNotNull();
    Assertions.assertThat(response.getStatus()).isEqualTo("Successful");
    // Mock the behavior of jwtService.generateToken

  }

  @Test
  void User_CreateUser_returnCreateResponse() {
    int userId = 123;
    Role role = Role.USER;
    // Create a mock User object
    User mockUser = new User();
    mockUser.setUserId(userId);
    mockUser.setRole(Role.USER);

    when(userDAO.findByEmail(Mockito.any())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(any())).thenReturn("1fsdfadfqrwgtwerert234");
    when(authenticationService.passwordGenerator(any())).thenReturn("123fwwretwertewrtefqwef4");

    String initialPassword = authenticationService.passwordGenerator("Hefqwewretwertwretfqwefqwello");
    CreateRequest request =
        CreateRequest.builder()
            .name("Albert Einstein")
            .email("admin@gmail.com")
            .phone("0972156450")
            .dob(new Date())
            .gender("Male")
            .role(Role.SUPER_ADMIN)
            .status("Wonderful")
            .createdBy("RankillerDY")
            .modifiedBy("Hoang Anh")
            .build();
    User user =
        User.builder()
            .name(request.getName())
            .password(passwordEncoder.encode(initialPassword))
            .email(request.getEmail())
            .phone(request.getPhone())
            .dob(request.getDob())
            .gender(request.getGender())
            .role(request.getRole())
            .status(request.getStatus())
            .createdBy(request.getCreatedBy())
            .modifiedBy(request.getModifiedBy())
            .build();

    when(userDAO.save(any())).thenReturn(user);
    when(emailService.sendMail(any())).thenReturn("Mail sent successfully");

    var existedUser = userDAO.findByEmail(user.getEmail()).orElse(null);
    logger.info("Existed user " + existedUser);

    var savedUser = userDAO.save(user);
    emailService.sendMail(EmailDetails.builder()
            .subject("Account Password")
            .build());

    CreateResponse response = authenticationService.createUser(request);
    logger.info("Response value: " + response.toString());
    Assertions.assertThat(response).isNotNull();
    Assertions.assertThat(response.getStatus()).isEqualTo("Successful");
  }
}
