package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateRequest;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ResponseObject> getAll();

    UpdateResponse updateUser(String userEmail, UpdateRequest updateRequest);

    ResponseObject deleteUser(String mail);
}
