package com.example.FAMS.services;

import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import java.util.Date;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ResponseObject> getAll();

    User updateUser(int userId, Role role, String name, String phone, Date dob, String gender, String status);


}
