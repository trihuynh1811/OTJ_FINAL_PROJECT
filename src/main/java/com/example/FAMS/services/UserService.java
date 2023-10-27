package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateRequest;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateResponse;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ResponseObject> getAll();

    ResponseEntity<ResponseObject> pagination(int pageNo);

    UpdateResponse updateUser(String userEmail, UpdateRequest updateRequest);

    ResponseObject deleteUser(String mail);

    ResponseEntity<ResponseObject> getAllTrainersByRole();

    ResponseEntity<ResponseObject> getAllAdminsByRole();

    ResponseEntity<ResponseObject> getAllAdminAndSuperAdminByRole();
}
