package com.example.FAMS.service_implementors;

import com.example.FAMS.controllers.UserController;
import com.example.FAMS.dto.UserDTO;
import com.example.FAMS.dto.requests.CreateRequest;
import com.example.FAMS.dto.requests.LoginRequest;
import com.example.FAMS.dto.responses.AuthenticationResponse;
import com.example.FAMS.dto.responses.CreateResponse;
import com.example.FAMS.dto.responses.LoginResponse;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.enums.TokenType;
import com.example.FAMS.models.EmailDetails;
import com.example.FAMS.models.Token;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.TokenDAO;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.services.AuthenticationService;
import com.example.FAMS.services.EmailService;
import com.example.FAMS.services.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserDAO userDAO;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final TokenDAO tokenDAO;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserPermissionDAO userPermissionDAO;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        var user = userDAO.findByEmail(loginRequest.getEmail())
                .orElseThrow();
        var token = jwtService.generateToken(user);
        revokeAllUserToken(user);
        saveUserToken(user, token);
        return LoginResponse.builder()
                .status("Successful")
                .role(user.getRole().getRole().name())
                .token(token)
                .userInfo(userDAO.findUserByEmail(loginRequest.getEmail()).orElse(null))
                .build();
    }

    @Override
    public CreateResponse createUser(CreateRequest createRequest) {
        var permission = userPermissionDAO.findUserPermissionByRole(createRequest.getRole()).orElse(null);
        String initialPassword = passwordGenerator(createRequest.getEmail());
        User user = User.builder()
                .name(createRequest.getName())
                .email(createRequest.getEmail())
                .password(passwordEncoder.encode(initialPassword))
                .phone(createRequest.getPhone())
                .dob(createRequest.getDob())
                .gender(createRequest.getGender())
                .role(permission)
                .status(createRequest.isStatus())
                .createdBy(createRequest.getCreatedBy())
                .createdDate(new Date())
                .modifiedBy(createRequest.getModifiedBy())
                .modifiedDate(new Date())
                .build();
        var existedUser = userDAO.findByEmail(user.getEmail()).orElse(null);
        if (existedUser == null) {
            var savedUser = userDAO.save(user);
            emailService.sendMail(EmailDetails.builder()
                    .subject("Account Password")
                    .msgBody(initialPassword)
                    .recipient(savedUser.getEmail())
                    .build());
            return CreateResponse.builder()
                    .status("Successful")
                    .createdUser(userDAO.findUserByEmail(savedUser.getEmail()).orElse(null))
                    .build();
        }
        return CreateResponse.builder()
                .status("Fail")
                .createdUser(null)
                .build();
    }

    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authenHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String userEmail;
        if (authenHeader == null || !authenHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authenHeader.substring(7);
        userEmail = jwtService.extractUserEmail(refreshToken);
        if (userEmail != null) {
            var existedUser = userDAO.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, existedUser)) {
                var newToken = jwtService.generateToken(existedUser);
                revokeAllUserToken(existedUser);
                saveUserToken(existedUser, newToken);
                var authResponse = AuthenticationResponse.builder()
                        .status("Successful")
                        .token(newToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public ResponseObject getLoggedInUser(HttpServletRequest request) {
        String authenHeader = request.getHeader("Authorization");
        final String token;
        final String userEmail;
        if (authenHeader == null || !authenHeader.startsWith("Bearer ")) {
            return null;
        }
        token = authenHeader.substring(7);
        userEmail = jwtService.extractUserEmail(token);
        if (userEmail != null) {
            var loggedInUser = userDAO.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(token, loggedInUser)) {
                UserDTO userDTO = userDAO.findUserByEmail(userEmail).orElse(null);
                return ResponseObject.builder()
                        .status("Successful")
                        .message(loggedInUser.getRole().getRole().name())
                        .payload(userDTO)
                        .build();
            }
        }
        return ResponseObject.builder()
                .status("Fail")
                .message("Invalid request")
                .build();
    }


    public String passwordGenerator(String email) {
        return passwordEncoder.encode(email).substring(9, 20);
    }

    public void saveUserToken(User user, String token) {
        var userToken = Token.builder()
                .token(token)
                .user(user)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenDAO.save(userToken);
    }

    public void revokeAllUserToken(User user) {
        var tokenList = tokenDAO.findAllUserTokenByUserId(user.getUserId());
        tokenList.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenDAO.saveAll(tokenList);
    }
}
