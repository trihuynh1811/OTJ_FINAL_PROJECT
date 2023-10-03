package com.example.FAMS.services;

import com.example.FAMS.dto.requests.CreateRequest;
import com.example.FAMS.dto.requests.LoginRequest;
import com.example.FAMS.dto.responses.CreateResponse;
import com.example.FAMS.dto.responses.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);

    CreateResponse createUser(CreateRequest createRequest);
}
