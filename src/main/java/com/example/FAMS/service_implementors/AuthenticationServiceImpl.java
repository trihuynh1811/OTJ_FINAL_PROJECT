package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.CreateRequest;
import com.example.FAMS.dto.requests.LoginRequest;
import com.example.FAMS.dto.responses.CreateResponse;
import com.example.FAMS.dto.responses.LoginResponse;
import com.example.FAMS.enums.TokenType;
import com.example.FAMS.models.EmailDetails;
import com.example.FAMS.models.Token;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.TokenDAO;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.services.AuthenticationService;
import com.example.FAMS.services.EmailService;
import com.example.FAMS.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDAO userDAO;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final TokenDAO tokenDAO;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        var user = userDAO.findByEmail(loginRequest.getEmail())
                .orElseThrow();
        var token = jwtService.generateToken(user);
        revokeAllUserToken(user);
        saveUserToken(user, token);
        return LoginResponse.builder()
//                Need something here
                .status("Successful")
                .token(token)
                .userInfo(userDAO.findUserByEmail(loginRequest.getEmail()).orElse(null))
                .build();
    }

    @Override
    public CreateResponse createUser(CreateRequest createRequest) {
        String initialPassword = passwordGenerator(createRequest.getEmail());
        User user = User.builder()
                .name(createRequest.getName())
                .email(createRequest.getEmail())
                .password(passwordEncoder.encode(initialPassword))
                .phone(createRequest.getPhone())
                .dob(createRequest.getDob())
                .gender(createRequest.getGender())
                .role(createRequest.getRole())
                .status(createRequest.getStatus())
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

    private String passwordGenerator(String email) {
        return passwordEncoder.encode(email).substring(0, 9);
    }

    private void saveUserToken(User user, String token) {
        var userToken = Token.builder()
                .token(token)
                .user(user)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenDAO.save(userToken);
    }

    private void revokeAllUserToken(User user) {
        var tokenList = tokenDAO.findAllUserTokenByUserId(user.getUserId());
        tokenList.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenDAO.saveAll(tokenList);
    }
}
