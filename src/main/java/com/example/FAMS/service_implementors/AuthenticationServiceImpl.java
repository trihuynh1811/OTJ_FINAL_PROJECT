package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.LoginRequest;
import com.example.FAMS.dto.responses.LoginResponse;
import com.example.FAMS.enums.TokenType;
import com.example.FAMS.models.Token;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.TokenDAO;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.services.AuthenticationService;
import com.example.FAMS.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDAO userDAO;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final TokenDAO tokenDAO;

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
//                Need something here
                .status("Successful")
                .accessToken(token)
                .build();
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
