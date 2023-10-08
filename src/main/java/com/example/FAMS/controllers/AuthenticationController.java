package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.CreateRequest;
import com.example.FAMS.dto.requests.LoginRequest;
import com.example.FAMS.dto.responses.CreateResponse;
import com.example.FAMS.dto.responses.LoginResponse;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @PostMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refresh(request, response);
    }

    @GetMapping("/login/get-user")
    public ResponseEntity<ResponseObject> getLoggedInUser(HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.getLoggedInUser(request));
    }
}
