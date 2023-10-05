package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateRequest;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateResponse;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import java.util.Date;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ResponseObject> getAll();

    UpdateResponse updateUser(UpdateRequest updateRequest);


}
