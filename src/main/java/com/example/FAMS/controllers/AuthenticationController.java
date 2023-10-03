package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.CreateRequest;
import com.example.FAMS.dto.requests.LoginRequest;
import com.example.FAMS.dto.responses.CreateResponse;
import com.example.FAMS.dto.responses.LoginResponse;
import com.example.FAMS.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginRequest(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping("/create-user")
    public ResponseEntity<CreateResponse> createUserRequest(@RequestBody CreateRequest createRequest) {
        return ResponseEntity.ok(authenticationService.createUser(createRequest));
    }
}
