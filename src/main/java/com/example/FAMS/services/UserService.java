package com.example.FAMS.services;

import com.example.FAMS.dto.responses.ResposeObject;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface UserService {
    ResponseEntity<ResposeObject> getAll();
    User updateUser(int userId, Role role, String name, String phone, Date dob, String gender, String status);
}
